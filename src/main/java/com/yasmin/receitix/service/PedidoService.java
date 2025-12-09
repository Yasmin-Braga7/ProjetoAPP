package com.yasmin.receitix.service;

import com.yasmin.receitix.DTO.request.PedidoDTORequest;
import com.yasmin.receitix.DTO.request.PedidoDTOUpdateRequest;
import com.yasmin.receitix.DTO.response.PedidoDTOResponse;
import com.yasmin.receitix.DTO.response.PedidoDTOUpdateResponse;
import com.yasmin.receitix.entity.Pedido;
import com.yasmin.receitix.repository.PedidoRepository;
import com.yasmin.receitix.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;


import com.yasmin.receitix.DTO.response.DashboardDTOResponse;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;

    private final UsuarioRepository usuarioRepository;
    @Autowired
    private ModelMapper modelMapper;

    public PedidoService(PedidoRepository pedidoRepository, UsuarioRepository usuarioRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Pedido> listarPedidos(){
        return this.pedidoRepository.listarPedidos();
    }

    public Pedido listarPorPedidoId(Integer pedidoId) {
        return this.pedidoRepository.obterPedidoPeloId(pedidoId);
    }

    public PedidoDTOResponse criarPedido(PedidoDTORequest pedidoDTOrequest) {

        Pedido pedido = new Pedido();
        pedido.setStatus(0);
        pedido.setTaxa(pedidoDTOrequest.getTaxa());
        pedido.setSubtotal(pedidoDTOrequest.getSubtotal());
        pedido.setCriado(LocalDateTime.now());
        pedido.setCriado(pedidoDTOrequest.getCriado());
        pedido.setTotal(pedidoDTOrequest.getTotal());
        pedido.setUsuario(usuarioRepository.obterUsuarioPeloId(pedidoDTOrequest.getIdUsuario()));
        Pedido pedidoSave = this.pedidoRepository.save(pedido);
        PedidoDTOResponse pedidoDTOResponse = modelMapper.map(pedidoSave, PedidoDTOResponse.class);
        return pedidoDTOResponse;
    }

    public PedidoDTOResponse atualizarPedido(@Valid Integer idPedido, PedidoDTORequest pedidoDTORequest) {
        Pedido pedido = this.listarPorPedidoId(idPedido);
        if (pedido!= null){
            pedido.setStatus(pedidoDTORequest.getStatus());
            pedido.setTaxa(pedidoDTORequest.getTaxa());
            pedido.setTotal(pedidoDTORequest.getTotal());
            pedido.setSubtotal(pedidoDTORequest.getSubtotal());
            pedido.setCriado(pedidoDTORequest.getCriado());
            pedido.setUsuario(usuarioRepository.obterUsuarioPeloId(pedidoDTORequest.getIdUsuario()));
            Pedido pedidoTemp = this.pedidoRepository.save(pedido);
            return modelMapper.map(pedidoTemp, PedidoDTOResponse.class);
        } else {
            return null;
        }
    }

    public PedidoDTOUpdateResponse atualizarStatusPedido(Integer pedidoId, PedidoDTOUpdateRequest pedidoDTOUpdateRequest) {
        //antes de atualizar busca se existe o registro a ser atualizar
        Pedido pedido = this.listarPorPedidoId(pedidoId);

        //se encontra o registro a ser atualizado
        if (pedido != null) {
            //atualizamos unicamente o campo de status
            pedido.setStatus(pedidoDTOUpdateRequest.getStatus());

            //com o objeto no formato correto tipo "participante" o comando "save" salva
            // no banco de dados o objeto atualizado
            Pedido tempResponse = pedidoRepository.save(pedido);
            return modelMapper.map(tempResponse, PedidoDTOUpdateResponse.class);
        }
        else{
            return null;
        }
    }

//    public DashboardDTOResponse gerarDadosDashboard() {
//        LocalDateTime agora = LocalDateTime.now();
//        LocalDateTime inicioMes = agora.with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0).withSecond(0);
//        LocalDateTime fimMes = agora.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59);
//
//        List<Pedido> pedidos = pedidoRepository.findByCriadoBetween(inicioMes, fimMes);
//
//        DashboardDTOResponse dto = new DashboardDTOResponse();
//
//        // 1. Totais Gerais
//        dto.setTotalPedidos(pedidos.size());
//        BigDecimal faturamento = pedidos.stream()
//                .map(Pedido::getTotal)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//        dto.setTotalFaturamento(faturamento);
//
//        // 2. Preparar dados do Gráfico (Agrupar por dia)
//        Map<String, Double> vendasPorDia = new TreeMap<>(); // TreeMap mantém ordem se usarmos chave ordenável, mas vamos formatar
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
//
//        // Inicializa o mapa para garantir que o gráfico não quebre se estiver vazio
//        // Opcional: preencher todos os dias do mês com 0, aqui faremos apenas dos dias com vendas para simplificar
//
//        for (Pedido p : pedidos) {
//            String dia = p.getCriado().format(formatter);
//            double valor = p.getTotal().doubleValue();
//            vendasPorDia.put(dia, vendasPorDia.getOrDefault(dia, 0.0) + valor);
//        }
//
//        // Separar em listas para o DTO
//        dto.setLabelsGrafico(new ArrayList<>(vendasPorDia.keySet()));
//        dto.setDataGrafico(new ArrayList<>(vendasPorDia.values()));
//
//        return dto;
//    }

    public DashboardDTOResponse gerarDadosDashboard() {
        // NÃO precisamos mais calcular datas de inicio e fim
        // LocalDateTime agora = LocalDateTime.now();
        // ... (pode apagar ou comentar as linhas de data)

        // --- ALTERAÇÃO RADICAL AQUI ---
        // Em vez de buscar por periodo, vamos buscar TUDO o que tem no banco.
        // O método findAll() já existe nativo no JpaRepository.
        List<Pedido> pedidos = pedidoRepository.findAll();

        // Adicione este print para ver no console do IntelliJ quantos pedidos ele achou
        System.out.println("DEBUG: Encontrados " + pedidos.size() + " pedidos no total.");

        DashboardDTOResponse dto = new DashboardDTOResponse();

        // 1. Totais Gerais
        dto.setTotalPedidos(pedidos.size());

        // O resto do código continua igual...
        BigDecimal faturamento = pedidos.stream()
                .map(Pedido::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dto.setTotalFaturamento(faturamento);

        // 2. Preparar dados do Gráfico
        Map<String, Double> vendasPorDia = new TreeMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        for (Pedido p : pedidos) {
            // Pequena proteção caso algum pedido antigo esteja sem data
            if(p.getCriado() != null) {
                String dia = p.getCriado().format(formatter);
                double valor = p.getTotal().doubleValue();
                vendasPorDia.put(dia, vendasPorDia.getOrDefault(dia, 0.0) + valor);
            }
        }

        dto.setLabelsGrafico(new ArrayList<>(vendasPorDia.keySet()));
        dto.setDataGrafico(new ArrayList<>(vendasPorDia.values()));

        return dto;
    }

    public void apagarPedido(Integer pedidoId){
        pedidoRepository.apagadoLogicoPedido(pedidoId);
    }
}
