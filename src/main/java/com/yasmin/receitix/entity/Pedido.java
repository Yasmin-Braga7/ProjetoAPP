package com.yasmin.receitix.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pedido_id")
    private int id;
    @Column(name = "pedido_subtotal")
    private BigDecimal subtotal;
    @Column(name = "pedido_taxa")
    private BigDecimal taxa;
    @Column(name = "pedido_total")
    private BigDecimal total;
    @Column(name = "pedido_status")
    private int status;
    @Column(name = "pedido_criado")
    private LocalDateTime criado;

    @OneToMany(mappedBy = "pedido")
    private Set<PedidoItem> pedidoItems;

    @Transient
    @JsonProperty("idUsuario")
    public int getIdUsuario(){
        return usuario!=null ? usuario.getId(): null;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

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

    public Set<PedidoItem> getPedidoItems() {
        return pedidoItems;
    }

    public void setPedidoItems(Set<PedidoItem> pedidoItems) {
        this.pedidoItems = pedidoItems;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
