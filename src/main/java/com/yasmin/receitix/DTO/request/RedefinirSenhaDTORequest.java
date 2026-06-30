package com.yasmin.receitix.DTO.request;

public record RedefinirSenhaDTORequest(
        String email,
        String resetToken,
        String novaSenha
) {
}
