package com.Yasmin.Receitix.DTO.request;

import jakarta.persistence.Column;

import java.math.BigDecimal;

public class PedidoItemDTORequest {
    private BigDecimal preco;

    private BigDecimal quantidade;

    private BigDecimal subtotal;

    private int status;

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
