package com.yasmin.receitix.DTO.response;

public record VerificarEmailDTOResponse(
        boolean encontrado,
        String mensagem,
        String emailMascarado,
        boolean telefoneDisponivel,
        String telefoneMascarado
) {
}
