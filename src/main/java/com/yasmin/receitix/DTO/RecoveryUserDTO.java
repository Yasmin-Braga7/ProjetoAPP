package com.yasmin.receitix.DTO;


import com.yasmin.receitix.entity.Role;

import java.util.List;

public record RecoveryUserDTO(
        Long id,
        String email,
        List<Role> roles
) {
}
