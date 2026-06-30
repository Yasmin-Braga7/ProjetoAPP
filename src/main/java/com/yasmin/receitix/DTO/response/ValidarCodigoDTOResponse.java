package com.yasmin.receitix.DTO.response;

public record ValidarCodigoDTOResponse(
        boolean valido,
        String mensagem,
        String resetToken
) {
}
