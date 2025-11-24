package com.Yasmin.Receitix.DTO;

import com.Yasmin.Receitix.DTO.response.UsuarioDTOResponse;
import com.Yasmin.Receitix.entity.RoleName;

public record CreateUserDTO(
        String nome,
        String email,
        String telefone,
        String senha,
        RoleName role
) {
}