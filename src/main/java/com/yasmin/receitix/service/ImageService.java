package com.yasmin.receitix.service;

import org.springframework.stereotype.Service;
import java.util.Base64;

@Service
public class ImageService {

    /**
     * Converte um array de bytes (representando uma imagem) para uma string Base64.
     * @param imageBytes O array de bytes da imagem.
     * @return A string Base64 da imagem.
     */
    public String convertImageToBase64(byte[] imageBytes) {
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    /**
     * Adiciona o prefixo de Data URI (data:image/jpeg;base64,) à string Base64.
     * O tipo MIME (image/jpeg) deve ser determinado dinamicamente ou inferido.
     * Para simplificar, usaremos um prefixo genérico ou o tipo fornecido.
     * @param base64String A string Base64 sem prefixo.
     * @param contentType O tipo MIME da imagem (ex: "image/jpeg", "image/png").
     * @return A string Base64 com o prefixo Data URI.
     */
    public String addDataUriPrefix(String base64String, String contentType) {
        return "data:" + contentType + ";base64," + base64String;
    }
}