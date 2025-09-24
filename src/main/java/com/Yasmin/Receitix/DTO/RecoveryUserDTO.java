package com.Yasmin.Receitix.DTO;


import com.Yasmin.Receitix.entity.Role;

import java.util.List;

public record RecoveryUserDTO(
        Long id,
        String email,
        List<Role> roles
) {
}
