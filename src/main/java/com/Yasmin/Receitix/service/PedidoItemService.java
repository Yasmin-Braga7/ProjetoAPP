package com.Yasmin.Receitix.service;

import com.Yasmin.Receitix.DTO.request.PedidoItemDTORequest;
import com.Yasmin.Receitix.DTO.request.PedidoItemDTOUpdateRequest;
import com.Yasmin.Receitix.DTO.response.PedidoItemDTOResponse;
import com.Yasmin.Receitix.DTO.response.PedidoItemDTOUpdateResponse;
import com.Yasmin.Receitix.entity.PedidoItem;
import com.Yasmin.Receitix.repository.PedidoItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoItemService {
    private final PedidoItemRepository pedidoItemRepository;
    @Autowired
    private ModelMapper modelMapper;

    public PedidoItemService(PedidoItemRepository pedidoItemRepository) {
        this.pedidoItemRepository = pedidoItemRepository;
    }

    public List<PedidoItem> listarPedidosItens(){
        return this.pedidoItemRepository.listarPedidosItens();
    }

    public PedidoItem listarPorPedidoItemId(Integer pedidoItemId) {
        return this.pedidoItemRepository.obterPedidoItemPeloId(pedidoItemId);
    }

    public PedidoItemDTOResponse criarPedidoItem(PedidoItemDTORequest pedidoItemDTORequest) {

        PedidoItem pedidoItem = modelMapper.map(pedidoItemDTORequest, PedidoItem.class);
        PedidoItem pedidoItemSave = this.pedidoItemRepository.save(pedidoItem);
        PedidoItemDTOResponse pedidoItemDTOResponse = modelMapper.map(pedidoItemSave, PedidoItemDTOResponse.class);
        return pedidoItemDTOResponse;
    }

    public PedidoItemDTOResponse atualizarPedidoItem(Integer pedidoItemId, PedidoItemDTORequest pedidoItemDTORequest) {
        //antes de atualizar busca se existe o registro a ser atualizar
        PedidoItem pedidoItem = this.listarPorPedidoItemId(pedidoItemId);

        //se encontra o registro a ser atualizado
        if (pedidoItem != null){
            //copia os dados a serem atualizados do DTO de entrada para um objeto do tipo participante
            //que é compatível com o repository para atualizar
            modelMapper.map(pedidoItemDTORequest,pedidoItem);

            //com o objeto no formato correto tipo "participante" o comando "save" salva
            // no banco de dados o objeto atualizado
            PedidoItem tempResponse = pedidoItemRepository.save(pedidoItem);

            return modelMapper.map(tempResponse, PedidoItemDTOResponse.class);
        }else {
            return null;
        }

    }

    public PedidoItemDTOUpdateResponse atualizarStatusPedidoItem(Integer pedidoItemId, PedidoItemDTOUpdateRequest pedidoItemDTOUpdateRequest) {
        //antes de atualizar busca se existe o registro a ser atualizar
        PedidoItem pedidoItem = this.listarPorPedidoItemId(pedidoItemId);

        //se encontra o registro a ser atualizado
        if (pedidoItem != null) {
            //atualizamos unicamente o campo de status
            pedidoItem.setStatus(PedidoItemDTOUpdateRequest.getStatus());

            //com o objeto no formato correto tipo "participante" o comando "save" salva
            // no banco de dados o objeto atualizado
            PedidoItem tempResponse = pedidoItemRepository.save(pedidoItem);
            return modelMapper.map(tempResponse, PedidoItemDTOUpdateResponse.class);
        }
        else{
            return null;
        }
    }

    public void apagarPedidoItem(Integer pedidoItemId){
        pedidoItemRepository.apagadoLogicoPedidoItem(pedidoItemId);
    }
}
