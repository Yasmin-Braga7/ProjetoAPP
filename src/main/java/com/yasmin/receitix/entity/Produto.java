package com.yasmin.receitix.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "produto")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="produto_id")
    private int id;
    @Column(name = "produto_nome")
    private String nome;
    @Column(name = "produto_descricao")
    private String descricao;
    @Column(name = "produto_preco")
    private BigDecimal preco;
    @Column(name = "produto_imagem")
    private byte[] imagem;
    @Column(name = "produto_criado")
    private LocalDateTime criado;
    @Column(name = "produto_status")
    private int status;

    @OneToOne(mappedBy = "produto")
    private PedidoItem pedidoItem;

    @Transient
    @JsonProperty("idCategoria")
    public int getIdCategoria(){
        return categoria !=null ? categoria.getId(): null;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name =  "categoria_id", nullable = false)
    private Categoria categoria;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
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

    public PedidoItem getPedidoItem() {
        return pedidoItem;
    }

    public void setPedidoItem(PedidoItem pedidoItem) {
        this.pedidoItem = pedidoItem;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}

