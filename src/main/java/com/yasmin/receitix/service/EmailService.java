package com.yasmin.receitix.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por enviar o código de verificação por e-mail durante
 * o fluxo de "Esqueci minha senha".
 *
 * Caso as propriedades de e-mail (spring.mail.*) não estejam configuradas no
 * application.properties, o JavaMailSender não é criado pelo Spring Boot e o
 * código simplesmente é exibido no console do servidor — isso permite testar
 * todo o fluxo em ambiente de desenvolvimento sem precisar de uma conta de
 * e-mail real configurada.
 */
@Service
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String remetente;

    public void enviarCodigoRecuperacao(String destinatario, String nomeUsuario, String codigo) {
        if (mailSender == null || remetente == null || remetente.isBlank()) {
            logFallback(destinatario, codigo);
            return;
        }

        try {
            SimpleMailMessage mensagem = new SimpleMailMessage();
            mensagem.setFrom(remetente);
            mensagem.setTo(destinatario);
            mensagem.setSubject("Recuperação de senha - DaniCake");
            mensagem.setText(
                    "Olá " + (nomeUsuario != null ? nomeUsuario : "") + ",\n\n" +
                    "Seu código de verificação para redefinir a senha é: " + codigo + "\n\n" +
                    "Esse código é válido por 10 minutos. Se você não solicitou essa alteração, apenas ignore este e-mail.\n\n" +
                    "Equipe DaniCake"
            );
            mailSender.send(mensagem);
        } catch (Exception e) {
            // Não deve quebrar o fluxo principal caso o envio falhe (ex: credenciais erradas).
            System.err.println("Erro ao enviar e-mail de recuperação: " + e.getMessage());
            logFallback(destinatario, codigo);
        }
    }

    private void logFallback(String destinatario, String codigo) {
        System.out.println("=========================================================");
        System.out.println("[DEV] E-mail não configurado (spring.mail.*). ");
        System.out.println("[DEV] Código de recuperação para " + destinatario + ": " + codigo);
        System.out.println("=========================================================");
    }
}
