package com.Yasmin.Receitix.DTO;

import com.Yasmin.Receitix.DTO.response.UsuarioDTOResponse;

public class RecoveryJwtTokenDTO {
    private String token;
    private UsuarioDTOResponse usuario;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UsuarioDTOResponse getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTOResponse usuario) {
        this.usuario = usuario;
    }
}
