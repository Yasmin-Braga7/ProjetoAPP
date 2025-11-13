package com.yasmin.receitix.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "categoria")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoria_id")
    private int id;
    @Column(name = "categoria_nome")
    private String nome;
    @Column(name = "categoria_criada")
    private LocalDateTime criada;
    @Column(name = "categoria_status")
    private int status;

    @OneToMany(mappedBy= "categoria")
    private Set<Produto> produtos;

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

    public LocalDateTime getCriada() {
        return criada;
    }

    public void setCriada(LocalDateTime criada) {
        this.criada = criada;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Set<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(Set<Produto> produtos) {
        this.produtos = produtos;
    }
}
