package com.yasmin.receitix.service;

import com.yasmin.receitix.DTO.request.EnviarCodigoDTORequest;
import com.yasmin.receitix.DTO.request.RedefinirSenhaDTORequest;
import com.yasmin.receitix.DTO.request.ValidarCodigoDTORequest;
import com.yasmin.receitix.DTO.response.MensagemDTOResponse;
import com.yasmin.receitix.DTO.response.ValidarCodigoDTOResponse;
import com.yasmin.receitix.DTO.response.VerificarEmailDTOResponse;
import com.yasmin.receitix.config.SecurityConfiguration;
import com.yasmin.receitix.entity.RecuperacaoSenha;
import com.yasmin.receitix.entity.Usuario;
import com.yasmin.receitix.enums.CanalRecuperacao;
import com.yasmin.receitix.repository.RecuperacaoSenhaRepository;
import com.yasmin.receitix.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Orquestra o fluxo completo de "Esqueci minha senha":
 *
 * 1) verificarEmail   -> confere se existe usuário com aquele e-mail
 * 2) enviarCodigo     -> gera um código de 6 dígitos e envia por e-mail ou SMS
 * 3) validarCodigo    -> confere o código digitado e gera um resetToken temporário
 * 4) redefinirSenha   -> troca a senha usando o resetToken validado
 */
@Service
public class RecuperacaoSenhaService {

    private static final int MINUTOS_VALIDADE_CODIGO = 10;
    private static final int MINUTOS_VALIDADE_TOKEN = 10;
    private static final int TAMANHO_MINIMO_SENHA = 6;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RecuperacaoSenhaRepository recuperacaoSenhaRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    // ───────────────────────── 1) Verificar e-mail ─────────────────────────

    public VerificarEmailDTOResponse verificarEmail(String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);

        if (usuario == null) {
            return new VerificarEmailDTOResponse(
                    false,
                    "Não encontramos nenhum usuário cadastrado com este e-mail.",
                    null,
                    false,
                    null
            );
        }

        boolean temTelefone = usuario.getTelefone() != null && !usuario.getTelefone().isBlank();

        return new VerificarEmailDTOResponse(
                true,
                "Usuário encontrado.",
                mascararEmail(usuario.getEmail()),
                temTelefone,
                temTelefone ? mascararTelefone(usuario.getTelefone()) : null
        );
    }

    // ───────────────────────── 2) Enviar código ─────────────────────────

    public MensagemDTOResponse enviarCodigo(EnviarCodigoDTORequest request) {
        Usuario usuario = buscarUsuarioPorEmail(request.email());

        if (usuario == null) {
            return new MensagemDTOResponse(false, "Não encontramos nenhum usuário cadastrado com este e-mail.");
        }

        if (request.canal() == null) {
            return new MensagemDTOResponse(false, "Selecione por onde deseja receber o código (e-mail ou SMS).");
        }

        if (request.canal() == CanalRecuperacao.SMS
                && (usuario.getTelefone() == null || usuario.getTelefone().isBlank())) {
            return new MensagemDTOResponse(false, "Este usuário não possui telefone cadastrado para receber SMS.");
        }

        // Invalida qualquer solicitação anterior desse usuário (só um código ativo por vez)
        recuperacaoSenhaRepository.apagarPorUsuario(usuario.getId());

        String codigo = gerarCodigo();

        RecuperacaoSenha recuperacao = new RecuperacaoSenha();
        recuperacao.setUsuarioId(usuario.getId());
        recuperacao.setEmail(usuario.getEmail());
        recuperacao.setCodigo(codigo);
        recuperacao.setCanal(request.canal());
        recuperacao.setCodigoExpiracao(LocalDateTime.now().plusMinutes(MINUTOS_VALIDADE_CODIGO));
        recuperacao.setCodigoValidado(false);
        recuperacao.setSenhaRedefinida(false);
        recuperacao.setCriado(LocalDateTime.now());

        recuperacaoSenhaRepository.save(recuperacao);

        if (request.canal() == CanalRecuperacao.SMS) {
            smsService.enviarCodigoRecuperacao(usuario.getTelefone(), codigo);
            return new MensagemDTOResponse(true, "Enviamos um código de verificação por SMS.");
        } else {
            emailService.enviarCodigoRecuperacao(usuario.getEmail(), usuario.getNome(), codigo);
            return new MensagemDTOResponse(true, "Enviamos um código de verificação para o seu e-mail.");
        }
    }

    // ───────────────────────── 3) Validar código ─────────────────────────

    public ValidarCodigoDTOResponse validarCodigo(ValidarCodigoDTORequest request) {
        Usuario usuario = buscarUsuarioPorEmail(request.email());

        if (usuario == null) {
            return new ValidarCodigoDTOResponse(false, "Usuário não encontrado.", null);
        }

        RecuperacaoSenha recuperacao = recuperacaoSenhaRepository.buscarPorUsuarioId(usuario.getId());

        if (recuperacao == null) {
            return new ValidarCodigoDTOResponse(false, "Nenhuma solicitação de recuperação encontrada. Solicite um novo código.", null);
        }

        if (recuperacao.getCodigoExpiracao().isBefore(LocalDateTime.now())) {
            return new ValidarCodigoDTOResponse(false, "O código expirou. Solicite um novo código.", null);
        }

        if (request.codigo() == null || !recuperacao.getCodigo().equals(request.codigo().trim())) {
            return new ValidarCodigoDTOResponse(false, "Código inválido. Verifique e tente novamente.", null);
        }

        String resetToken = UUID.randomUUID().toString();
        recuperacao.setCodigoValidado(true);
        recuperacao.setResetToken(resetToken);
        recuperacao.setTokenExpiracao(LocalDateTime.now().plusMinutes(MINUTOS_VALIDADE_TOKEN));
        recuperacaoSenhaRepository.save(recuperacao);

        return new ValidarCodigoDTOResponse(true, "Código validado com sucesso.", resetToken);
    }

    // ───────────────────────── 4) Redefinir senha ─────────────────────────

    public MensagemDTOResponse redefinirSenha(RedefinirSenhaDTORequest request) {
        Usuario usuario = buscarUsuarioPorEmail(request.email());

        if (usuario == null) {
            return new MensagemDTOResponse(false, "Usuário não encontrado.");
        }

        if (request.resetToken() == null || request.resetToken().isBlank()) {
            return new MensagemDTOResponse(false, "Solicitação de recuperação inválida. Refaça o processo.");
        }

        RecuperacaoSenha recuperacao = recuperacaoSenhaRepository.buscarPorResetToken(request.resetToken());

        if (recuperacao == null || !recuperacao.getUsuarioId().equals(usuario.getId())) {
            return new MensagemDTOResponse(false, "Solicitação de recuperação inválida. Refaça o processo.");
        }

        if (!recuperacao.isCodigoValidado()) {
            return new MensagemDTOResponse(false, "Você precisa validar o código antes de redefinir a senha.");
        }

        if (recuperacao.isSenhaRedefinida()) {
            return new MensagemDTOResponse(false, "Este código já foi utilizado. Solicite um novo código.");
        }

        if (recuperacao.getTokenExpiracao() == null || recuperacao.getTokenExpiracao().isBefore(LocalDateTime.now())) {
            return new MensagemDTOResponse(false, "Sua sessão de recuperação expirou. Solicite um novo código.");
        }

        if (request.novaSenha() == null || request.novaSenha().length() < TAMANHO_MINIMO_SENHA) {
            return new MensagemDTOResponse(false, "A nova senha deve ter pelo menos " + TAMANHO_MINIMO_SENHA + " caracteres.");
        }

        usuario.setSenha(securityConfiguration.passwordEncoder().encode(request.novaSenha()));
        usuarioRepository.save(usuario);

        recuperacao.setSenhaRedefinida(true);
        recuperacaoSenhaRepository.save(recuperacao);

        return new MensagemDTOResponse(true, "Senha redefinida com sucesso! Faça login com sua nova senha.");
    }

    // ───────────────────────── Auxiliares ─────────────────────────

    private Usuario buscarUsuarioPorEmail(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }
        return usuarioRepository.findByEmail(email.trim());
    }

    private String gerarCodigo() {
        SecureRandom random = new SecureRandom();
        int numero = 100000 + random.nextInt(900000); // sempre 6 dígitos (100000-999999)
        return String.valueOf(numero);
    }

    private String mascararEmail(String email) {
        int arroba = email.indexOf('@');
        if (arroba <= 1) {
            return "***" + email.substring(arroba);
        }
        String inicio = email.substring(0, Math.min(2, arroba));
        return inicio + "***" + email.substring(arroba);
    }

    private String mascararTelefone(String telefone) {
        String apenasNumeros = telefone.replaceAll("\\D", "");
        if (apenasNumeros.length() < 4) {
            return "****";
        }
        String ultimosDigitos = apenasNumeros.substring(apenasNumeros.length() - 4);
        return "(**) *****-" + ultimosDigitos;
    }
}
