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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;
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

    public DashboardDTOResponse obterDadosDashboard() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime inicioMes = agora.with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime fimMes = agora.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59);

        List<Pedido> pedidosDoMes = pedidoRepository.encontrarPedidosNoPeriodo(inicioMes, fimMes);

        DashboardDTOResponse dto = new DashboardDTOResponse();

        // 1. Quantidade total
        dto.setQuantidadePedidosMes(pedidosDoMes.size());

        // 2. Faturamento total (soma)
        BigDecimal total = pedidosDoMes.stream()
                .map(Pedido::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dto.setFaturamentoTotalMes(total);

        // 3. Agrupamento por dia para o gráfico
        // Formata a data como "dd/MM" e soma os valores daquele dia
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        Map<String, BigDecimal> vendasMap = pedidosDoMes.stream()
                .sorted((p1, p2) -> p1.getCriado().compareTo(p2.getCriado())) // Garante ordem cronológica
                .collect(Collectors.groupingBy(
                        p -> p.getCriado().format(formatter),
                        LinkedHashMap::new,
                        Collectors.reducing(BigDecimal.ZERO, Pedido::getTotal, BigDecimal::add)
                ));

        dto.setVendasPorDia(vendasMap);

        return dto;
    }

    public void apagarPedido(Integer pedidoId){
        pedidoRepository.apagadoLogicoPedido(pedidoId);
    }
}
