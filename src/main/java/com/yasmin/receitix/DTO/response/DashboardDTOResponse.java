package com.yasmin.receitix.DTO.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DashboardDTOResponse {
    private long quantidadePedidosMes;
    private BigDecimal faturamentoTotalMes;
    // Lista para o gráfico (Ex: "12/05" -> 150.00)
    private Map<String, BigDecimal> vendasPorDia;

    // Getters e Setters
    public long getQuantidadePedidosMes() { return quantidadePedidosMes; }
    public void setQuantidadePedidosMes(long quantidadePedidosMes) { this.quantidadePedidosMes = quantidadePedidosMes; }

    public BigDecimal getFaturamentoTotalMes() { return faturamentoTotalMes; }
    public void setFaturamentoTotalMes(BigDecimal faturamentoTotalMes) { this.faturamentoTotalMes = faturamentoTotalMes; }

    public Map<String, BigDecimal> getVendasPorDia() { return vendasPorDia; }
    public void setVendasPorDia(Map<String, BigDecimal> vendasPorDia) { this.vendasPorDia = vendasPorDia; }
}