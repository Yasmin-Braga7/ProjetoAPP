package com.Yasmin.Receitix.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "pedido_item")
public class PedidoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pedido_item_id")
    private int id;
    @Column(name="pedido_item_preco")
    private BigDecimal preco;
    @Column(name="pedido_item_quantidade")
    private BigDecimal quantidade;
    @Column(name = "pedido_item_subtotal")
    private BigDecimal subtotal;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @Transient
    @JsonProperty("idProduto")
    public int getIdProduto() {
        return produto != null ? produto.getId() : null;
    }
    @Transient
    @JsonProperty("idPedido")
    public int getIdPedido(){
        return pedido !=null ? pedido.getId(): null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
}
