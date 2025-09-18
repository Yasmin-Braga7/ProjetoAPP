package com.Yasmin.Receitix.service;

import com.Yasmin.Receitix.DTO.response.PedidoItemDTOResponse;
import com.Yasmin.Receitix.entity.PedidoItem;
import com.Yasmin.Receitix.repository.PedidoItemRepository;
import com.Yasmin.Receitix.repository.PedidoRepository;
import com.Yasmin.Receitix.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoItemService {

    private final PedidoItemRepository pedidoItemRepository;
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoItemService(PedidoItemRepository pedidoItemRepository, PedidoRepository pedidoRepository, ProdutoRepository produtoRepository) {
        this.pedidoItemRepository = pedidoItemRepository;
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
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

