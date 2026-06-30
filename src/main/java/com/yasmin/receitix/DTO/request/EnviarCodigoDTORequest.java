package com.yasmin.receitix.DTO.request;

import com.yasmin.receitix.enums.CanalRecuperacao;

public record EnviarCodigoDTORequest(
        String email,
        CanalRecuperacao canal
) {
}
