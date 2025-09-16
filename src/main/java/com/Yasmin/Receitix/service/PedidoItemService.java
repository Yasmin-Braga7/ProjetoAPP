package com.Yasmin.Receitix.service;

import com.Yasmin.Receitix.entity.PedidoItem;
import com.Yasmin.Receitix.repository.PedidoItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoItemService {

    private final PedidoItemRepository pedidoItemRepository;

    public PedidoItemService(PedidoItemRepository pedidoItemRepository) {
        this.pedidoItemRepository = pedidoItemRepository;
    }

    public List<PedidoItem> listarPedidoItens(){
        return this.pedidoItemRepository.listarPedidoItens();
    }

    public PedidoItem listarPorPedidoItemId(Integer pedidoItemId) {
        return this.pedidoItemRepository.obterPedidoItemPeloId(pedidoItemId);
    }

    public void apagarPedidoItem(Integer pedidoItemId){
        pedidoItemRepository.apagadoLogicoPedidoItem(pedidoItemId);
    }
}

