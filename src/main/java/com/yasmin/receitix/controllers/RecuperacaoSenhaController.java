package com.yasmin.receitix.controllers;

import com.yasmin.receitix.DTO.request.EnviarCodigoDTORequest;
import com.yasmin.receitix.DTO.request.RedefinirSenhaDTORequest;
import com.yasmin.receitix.DTO.request.ValidarCodigoDTORequest;
import com.yasmin.receitix.DTO.request.VerificarEmailDTORequest;
import com.yasmin.receitix.DTO.response.MensagemDTOResponse;
import com.yasmin.receitix.DTO.response.ValidarCodigoDTOResponse;
import com.yasmin.receitix.DTO.response.VerificarEmailDTOResponse;
import com.yasmin.receitix.service.RecuperacaoSenhaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/usuario/recuperarSenha")
@Tag(name = "Recuperação de Senha", description = "Endpoints do fluxo de 'Esqueci minha senha'")
@CrossOrigin("*")
public class RecuperacaoSenhaController {

    private final RecuperacaoSenhaService recuperacaoSenhaService;

    public RecuperacaoSenhaController(RecuperacaoSenhaService recuperacaoSenhaService) {
        this.recuperacaoSenhaService = recuperacaoSenhaService;
    }

    @PostMapping("/verificarEmail")
    @Operation(summary = "Verificar e-mail", description = "Verifica se existe um usuário cadastrado com o e-mail informado")
    public ResponseEntity<VerificarEmailDTOResponse> verificarEmail(@RequestBody VerificarEmailDTORequest request) {
        return ResponseEntity.ok(recuperacaoSenhaService.verificarEmail(request.email()));
    }

    @PostMapping("/enviarCodigo")
    @Operation(summary = "Enviar código", description = "Gera e envia um código de verificação por e-mail ou SMS")
    public ResponseEntity<MensagemDTOResponse> enviarCodigo(@RequestBody EnviarCodigoDTORequest request) {
        return ResponseEntity.ok(recuperacaoSenhaService.enviarCodigo(request));
    }

    @PostMapping("/validarCodigo")
    @Operation(summary = "Validar código", description = "Valida o código de verificação informado e retorna um token para redefinir a senha")
    public ResponseEntity<ValidarCodigoDTOResponse> validarCodigo(@RequestBody ValidarCodigoDTORequest request) {
        return ResponseEntity.ok(recuperacaoSenhaService.validarCodigo(request));
    }

    @PostMapping("/redefinirSenha")
    @Operation(summary = "Redefinir senha", description = "Redefine a senha do usuário usando o token gerado após validar o código")
    public ResponseEntity<MensagemDTOResponse> redefinirSenha(@RequestBody RedefinirSenhaDTORequest request) {
        return ResponseEntity.ok(recuperacaoSenhaService.redefinirSenha(request));
    }
}
