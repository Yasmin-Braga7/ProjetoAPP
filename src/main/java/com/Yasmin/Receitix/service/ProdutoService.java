package com.Yasmin.Receitix.service;


import com.Yasmin.Receitix.DTO.request.ProdutoDTORequest;
import com.Yasmin.Receitix.DTO.request.ProdutoDTOUpdateRequest;
import com.Yasmin.Receitix.DTO.response.ProdutoDTOResponse;
import com.Yasmin.Receitix.DTO.response.ProdutoDTOUpdateResponse;
import com.Yasmin.Receitix.entity.Produto;
import com.Yasmin.Receitix.repository.ProdutoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    @Autowired
    private ModelMapper modelMapper;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> listarProdutos(){
        return this.produtoRepository.listarProdutos();
    }

    public Produto listarPorProdutoId(Integer produtoId) {
        return this.produtoRepository.obterProdutoPeloId(produtoId);
    }

    public ProdutoDTOResponse criarProduto(ProdutoDTORequest produtoDTORequest) {

        Produto produto = modelMapper.map(produtoDTORequest, Produto.class);
        Produto produtoSave = this.produtoRepository.save(produto);
        ProdutoDTOResponse produtoDTOResponse = modelMapper.map(produtoSave, ProdutoDTOResponse.class);
        return produtoDTOResponse;
    }

    public ProdutoDTOResponse atualizarProduto(Integer produtoId, ProdutoDTORequest produtoDTORequest) {
        //antes de atualizar busca se existe o registro a ser atualizar
        Produto produto = this.listarPorProdutoId(produtoId);

        //se encontra o registro a ser atualizado
        if (produto != null){
            //copia os dados a serem atualizados do DTO de entrada para um objeto do tipo participante
            //que é compatível com o repository para atualizar
            modelMapper.map(produtoDTORequest,produto);

            //com o objeto no formato correto tipo "participante" o comando "save" salva
            // no banco de dados o objeto atualizado
            Produto tempResponse = produtoRepository.save(produto);

            return modelMapper.map(tempResponse, ProdutoDTOResponse.class);
        }else {
            return null;
        }

    }

    public ProdutoDTOUpdateResponse atualizarStatusParticipante(Integer produtoId, ProdutoDTOUpdateRequest produtoDTOUpdateRequest) {
        //antes de atualizar busca se existe o registro a ser atualizar
        Produto produto = this.listarPorProdutoId(produtoId);

        //se encontra o registro a ser atualizado
        if (produto != null) {
            //atualizamos unicamente o campo de status
            produto.setStatus(produtoDTOUpdateRequest.getStatus());

            //com o objeto no formato correto tipo "participante" o comando "save" salva
            // no banco de dados o objeto atualizado
            Produto tempResponse = produtoRepository.save(produto);
            return modelMapper.map(tempResponse, ProdutoDTOUpdateResponse.class);
        }
        else{
            return null;
        }
    }

    public void apagarProduto(Integer produtoId){
        produtoRepository.apagadoLogicoProduto(produtoId);
    }
}
