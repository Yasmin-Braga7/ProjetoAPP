package com.Yasmin.Receitix.service;

import com.Yasmin.Receitix.DTO.request.PedidoDTORequest;
import com.Yasmin.Receitix.DTO.request.PedidoDTOUpdateRequest;
import com.Yasmin.Receitix.DTO.response.PedidoDTOResponse;
import com.Yasmin.Receitix.DTO.response.PedidoDTOUpdateResponse;
import com.Yasmin.Receitix.entity.Pedido;
import com.Yasmin.Receitix.repository.PedidoRepository;
import com.Yasmin.Receitix.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        pedido.setStatus(pedidoDTOrequest.getStatus());
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

//    public PedidoDTOResponse atualizarPedido(Integer pedidoId, PedidoDTORequest pedidoDTORequest) {
//        //antes de atualizar busca se existe o registro a ser atualizar
//        Pedido pedido = this.listarPorPedidoId(pedidoId);
//
//        //se encontra o registro a ser atualizado
//        if (pedido != null){
//            //copia os dados a serem atualizados do DTO de entrada para um objeto do tipo participante
//            //que é compatível com o repository para atualizar
//            modelMapper.map(pedidoDTORequest,pedido);
//
//            //com o objeto no formato correto tipo "participante" o comando "save" salva
//            // no banco de dados o objeto atualizado
//            Pedido tempResponse = pedidoRepository.save(pedido);
//
//            return modelMapper.map(tempResponse, PedidoDTOResponse.class);
//        }else {
//            return null;
//        }
//
//    }

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

    public void apagarPedido(Integer pedidoId){
        pedidoRepository.apagadoLogicoPedido(pedidoId);
    }
}
