package com.Yasmin.Receitix.DTO.request;

import jakarta.persistence.Column;

import java.time.LocalDateTime;

public class CategoriaDTORequest {
    private String nome;

    private LocalDateTime data;

    private int status;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
