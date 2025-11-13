package com.yasmin.receitix.service;

import com.yasmin.receitix.DTO.request.PedidoItemDTORequest;
import com.yasmin.receitix.DTO.response.PedidoItemDTOResponse;
import com.yasmin.receitix.entity.PedidoItem;
import com.yasmin.receitix.repository.PedidoItemRepository;
import com.yasmin.receitix.repository.PedidoRepository;
import com.yasmin.receitix.repository.ProdutoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoItemService {

    private final PedidoItemRepository pedidoItemRepository;
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PedidoItemService(PedidoItemRepository pedidoItemRepository, PedidoRepository pedidoRepository, ProdutoRepository produtoRepository) {
        this.pedidoItemRepository = pedidoItemRepository;
        this.produtoRepository = produtoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public List<PedidoItem> listarPedidoItens(){
        return this.pedidoItemRepository.listarPedidoItens();
    }

    public PedidoItem listarPorPedidoItemId(Integer pedidoItemId) {
        return this.pedidoItemRepository.obterPedidoItemPeloId(pedidoItemId);
    }

    public PedidoItemDTOResponse criarPedidoItem(PedidoItemDTORequest pedidoItemDTORequest){
        PedidoItem pedidoItem = new PedidoItem();
        pedidoItem.setPreco(pedidoItemDTORequest.getPreco());
        pedidoItem.setQuantidade(pedidoItemDTORequest.getQuantidade());
        pedidoItem.setSubtotal(pedidoItemDTORequest.getSubtotal());
        pedidoItem.setPedido(pedidoRepository.obterPedidoPeloId(pedidoItemDTORequest.getIdPedido()));
        pedidoItem.setProduto(produtoRepository.obterProdutoPeloId(pedidoItemDTORequest.getIdProduto()));
        PedidoItem pedidoItemSave = this.pedidoItemRepository.save(pedidoItem);
        PedidoItemDTOResponse pedidoItemDTOResponse = modelMapper.map(pedidoItemSave, PedidoItemDTOResponse.class);
        return pedidoItemDTOResponse;
    }

    public void apagarPedidoItem(Integer pedidoItemId){
        pedidoItemRepository.apagadoLogicoPedidoItem(pedidoItemId);
    }
}

