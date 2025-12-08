package com.yasmin.receitix.enums;

public enum StatusPedido {
    CANCELADO(-1),
    PENDENTE(0),
    EM_PREPARO(1),
    ENVIADO(2),
    ENTREGUE(3);

    private final int codigo;

    StatusPedido(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static StatusPedido toEnum(Integer cod) {
        if (cod == null) return null;
        for (StatusPedido x : StatusPedido.values()) {
            if (cod.equals(x.getCodigo())) {
                return x;
            }
        }
        throw new IllegalArgumentException("Id inválido: " + cod);
    }
}