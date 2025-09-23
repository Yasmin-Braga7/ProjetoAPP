package com.Yasmin.Receitix.DTO;

import com.Yasmin.Receitix.entity.RoleName;

public record CreateUserDTO(
        String email,
        String password,
        RoleName role
) {
}
