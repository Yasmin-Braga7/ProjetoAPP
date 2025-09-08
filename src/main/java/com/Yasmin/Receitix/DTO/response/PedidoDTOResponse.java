package com.Yasmin.Receitix.DTO.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PedidoDTOResponse {

    private int id;
    private BigDecimal subtotal;

    private BigDecimal taxa;

    private BigDecimal total;

    private int status;

    private LocalDateTime criado;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTaxa() {
        return taxa;
    }

    public void setTaxa(BigDecimal taxa) {
        this.taxa = taxa;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getCriado() {
        return criado;
    }

    public void setCriado(LocalDateTime criado) {
        this.criado = criado;
    }
}
