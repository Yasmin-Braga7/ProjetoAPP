package com.Yasmin.Receitix.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "bloqueio")
public class Bloqueio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bloqueio_id")
    private int id;
    @Column(name = "bloqueio_data")
    private LocalDate data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}
