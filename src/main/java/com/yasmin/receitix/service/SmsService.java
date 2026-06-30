package com.yasmin.receitix.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Serviço responsável por enviar o código de verificação por SMS durante o
 * fluxo de "Esqueci minha senha", usando a API REST da Twilio
 * (https://www.twilio.com/docs/sms/send-messages).
 *
 * Caso as credenciais da Twilio (twilio.account.sid, twilio.auth.token,
 * twilio.phone.number) não estejam configuradas no application.properties,
 * o código é apenas exibido no console do servidor — isso permite testar
 * todo o fluxo em ambiente de desenvolvimento sem precisar de uma conta
 * Twilio paga/configurada.
 *
 * Para habilitar o envio real:
 * 1. Crie uma conta em https://www.twilio.com/try-twilio
 * 2. Pegue o Account SID, Auth Token e compre/use um número de telefone Twilio
 * 3. Preencha as propriedades twilio.account.sid, twilio.auth.token e
 *    twilio.phone.number no application.properties
 */
@Service
public class SmsService {

    private static final String TWILIO_URL_BASE = "https://api.twilio.com/2010-04-01/Accounts/";

    @Value("${twilio.account.sid:}")
    private String accountSid;

    @Value("${twilio.auth.token:}")
    private String authToken;

    @Value("${twilio.phone.number:}")
    private String numeroOrigem;

    private final RestTemplate restTemplate = new RestTemplate();

    public void enviarCodigoRecuperacao(String numeroDestino, String codigo) {
        if (!estaConfigurado()) {
            logFallback(numeroDestino, codigo);
            return;
        }

        try {
            String url = TWILIO_URL_BASE + accountSid + "/Messages.json";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBasicAuth(accountSid, authToken);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("To", formatarNumero(numeroDestino));
            body.add("From", numeroOrigem);
            body.add("Body", "Seu código de recuperação de senha DaniCake é: " + codigo + " (válido por 10 minutos)");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            restTemplate.postForObject(url, request, String.class);
        } catch (Exception e) {
            // Não deve quebrar o fluxo principal caso o envio falhe.
            System.err.println("Erro ao enviar SMS de recuperação: " + e.getMessage());
            logFallback(numeroDestino, codigo);
        }
    }

    private boolean estaConfigurado() {
        return accountSid != null && !accountSid.isBlank()
                && authToken != null && !authToken.isBlank()
                && numeroOrigem != null && !numeroOrigem.isBlank();
    }

    // Garante o formato E.164 (+5521999999999) exigido pela Twilio.
    // Se o telefone cadastrado não tiver o "+", assume o DDI do Brasil (+55).
    private String formatarNumero(String numero) {
        String apenasNumeros = numero.replaceAll("[^0-9+]", "");
        if (apenasNumeros.startsWith("+")) {
            return apenasNumeros;
        }
        return "+55" + apenasNumeros;
    }

    private void logFallback(String numeroDestino, String codigo) {
        System.out.println("=========================================================");
        System.out.println("[DEV] SMS não configurado (twilio.*). ");
        System.out.println("[DEV] Código de recuperação para " + numeroDestino + ": " + codigo);
        System.out.println("=========================================================");
    }
}
