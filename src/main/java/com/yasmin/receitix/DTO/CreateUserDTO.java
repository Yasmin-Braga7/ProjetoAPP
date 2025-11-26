package com.yasmin.receitix.DTO;

import com.yasmin.receitix.entity.RoleName;

public record CreateUserDTO(
        String nome,
        String email,
        String senha,
        RoleName role
) {
}
