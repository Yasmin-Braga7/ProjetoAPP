package com.yasmin.receitix.DTO.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DashboardDTOResponse {
    private BigDecimal totalFaturamento;
    private long totalPedidos;

    private List<String> labelsGrafico;
    private List<Double> dataGrafico;

    public BigDecimal getTotalFaturamento() { return totalFaturamento; }
    public void setTotalFaturamento(BigDecimal totalFaturamento) { this.totalFaturamento = totalFaturamento; }

    public long getTotalPedidos() { return totalPedidos; }
    public void setTotalPedidos(long totalPedidos) { this.totalPedidos = totalPedidos; }

    public List<String> getLabelsGrafico() { return labelsGrafico; }
    public void setLabelsGrafico(List<String> labelsGrafico) { this.labelsGrafico = labelsGrafico; }

    public List<Double> getDataGrafico() { return dataGrafico; }
    public void setDataGrafico(List<Double> dataGrafico) { this.dataGrafico = dataGrafico; }
}