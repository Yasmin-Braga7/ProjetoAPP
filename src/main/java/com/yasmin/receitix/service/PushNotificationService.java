package com.yasmin.receitix.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Serviço responsável por enviar notificações push via Expo Push API.
 * Documentação: https://docs.expo.dev/push-notifications/sending-notifications/
 */
@Service
public class PushNotificationService {

    private static final String EXPO_PUSH_URL = "https://exp.host/--/api/v2/push/send";

    private final RestTemplate restTemplate = new RestTemplate();

    public void enviar(String expoPushToken, String titulo, String corpo, Map<String, Object> dados) {
        if (expoPushToken == null || expoPushToken.isBlank()) {
            return;
        }

        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("to", expoPushToken);
            payload.put("title", titulo);
            payload.put("body", corpo);
            payload.put("data", dados);
            payload.put("sound", "default");
            payload.put("channelId", "pedidos");
            payload.put("priority", "high");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Accept", "application/json");

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

            restTemplate.postForObject(EXPO_PUSH_URL, request, String.class);
        } catch (Exception e) {
            // Não deve quebrar o fluxo principal (criação de pedido, login, etc.)
            // caso o envio da notificação falhe.
            System.err.println("Erro ao enviar notificação push: " + e.getMessage());
        }
    }
}
