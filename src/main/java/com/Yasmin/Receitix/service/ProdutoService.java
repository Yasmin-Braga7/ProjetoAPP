package com.Yasmin.Receitix.service;


import com.Yasmin.Receitix.DTO.request.ProdutoDTORequest;
import com.Yasmin.Receitix.DTO.request.ProdutoDTOUpdateRequest;
import com.Yasmin.Receitix.DTO.response.ProdutoDTOResponse;
import com.Yasmin.Receitix.DTO.response.ProdutoDTOUpdateResponse;
import com.Yasmin.Receitix.entity.Produto;
import com.Yasmin.Receitix.repository.CategoriaRepository;
import com.Yasmin.Receitix.repository.ProdutoRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    private final CategoriaRepository categoriaRepository;
    @Autowired
    private ModelMapper modelMapper;

    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<Produto> listarProdutos(){
        return this.produtoRepository.listarProdutos();
    }

    public Produto listarPorProdutoId(Integer produtoId) {
        return this.produtoRepository.obterProdutoPeloId(produtoId);
    }

    public ProdutoDTOResponse criarProduto(ProdutoDTORequest produtoDTORequest) {

        Produto produto = new Produto();
        produto.setCriado(produtoDTORequest.getCriado());
        produto.setDescricao(produtoDTORequest.getDescricao());
        produto.setStatus(produtoDTORequest.getStatus());
        produto.setImagem(produtoDTORequest.getImagem());
        produto.setNome(produtoDTORequest.getNome());
        produto.setPreco(produtoDTORequest.getPreco());
        produto.setCategoria(categoriaRepository.obterCategoriaPeloId(produtoDTORequest.getIdCategoria()));
        Produto produtoSave = this.produtoRepository.save(produto);
        ProdutoDTOResponse produtoDTOResponse = modelMapper.map(produtoSave, ProdutoDTOResponse.class);
        return produtoDTOResponse;
    }

    public ProdutoDTOResponse atualizarProduto(@Valid Integer idPedido, ProdutoDTORequest produtoDTORequest) {
        Produto produto = this.listarPorProdutoId(idPedido);

        if (produto != null){
            produto.setCriado(produtoDTORequest.getCriado());
            produto.setDescricao(produtoDTORequest.getDescricao());
            produto.setStatus(produtoDTORequest.getStatus());
            produto.setImagem(produtoDTORequest.getImagem());
            produto.setNome(produtoDTORequest.getNome());
            produto.setPreco(produtoDTORequest.getPreco());
            produto.setCategoria(categoriaRepository.obterCategoriaPeloId(produtoDTORequest.getIdCategoria()));
            Produto produtoTemp = this.produtoRepository.save(produto);
            return modelMapper.map(produtoTemp, ProdutoDTOResponse.class);
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
