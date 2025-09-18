package com.Yasmin.Receitix.service;

import com.Yasmin.Receitix.DTO.request.PedidoItemDTORequest;
import com.Yasmin.Receitix.DTO.response.PedidoItemDTOResponse;
import com.Yasmin.Receitix.entity.PedidoItem;
import com.Yasmin.Receitix.repository.PedidoItemRepository;
import com.Yasmin.Receitix.repository.PedidoRepository;
import com.Yasmin.Receitix.repository.ProdutoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoItemService {

    private final PedidoItemRepository pedidoItemRepository;
    private final ProdutoRepository produtoRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PedidoItemService(PedidoItemRepository pedidoItemRepository, PedidoRepository pedidoRepository, ProdutoRepository produtoRepository) {
        this.pedidoItemRepository = pedidoItemRepository;
        this.produtoRepository = produtoRepository;
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
        pedidoItem.setPedido(pedidoItemRepository.obterPedidoItemPeloId(pedidoItemDTORequest.getIdPedido());
        pedidoItem.setProduto(produtoRepository.obterProdutoPeloId(pedidoItemDTORequest.getIdProduto()));
        PedidoItem pedidoItemSave = this.pedidoItemRepository.save(pedidoItem);
        PedidoItemDTOResponse pedidoItemDTOResponse = modelMapper.map(pedidoItemSave, PedidoItemDTOResponse.class);
        return pedidoItemDTOResponse;
    }

    public void apagarPedidoItem(Integer pedidoItemId){
        pedidoItemRepository.apagadoLogicoPedidoItem(pedidoItemId);
    }
}

