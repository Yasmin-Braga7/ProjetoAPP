package com.Yasmin.Receitix.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "categoria_produto")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoria_produto_id")
    private int id;
    @Column(name = "categoria_produto_nome")
    private String nome;
    @Column(name = "categoria_produto_criada")
    private LocalDateTime data;
    @Column(name = "categoria_produto_status")
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
