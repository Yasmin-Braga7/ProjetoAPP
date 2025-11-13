package com.yasmin.receitix.DTO.request;

import jakarta.persistence.Column;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProdutoDTORequest {
    private String nome;

    private String descricao;

    private BigDecimal preco;

    private String imagem;

    private LocalDateTime criado;

    private int status;

    private int idCategoria;

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public LocalDateTime getCriado() {
        return criado;
    }

    public void setCriado(LocalDateTime criado) {
        this.criado = criado;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
