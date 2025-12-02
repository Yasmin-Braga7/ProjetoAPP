package com.yasmin.receitix.service;


import com.yasmin.receitix.DTO.request.ProdutoDTORequest;
import com.yasmin.receitix.DTO.request.ProdutoDTOUpdateRequest;
import com.yasmin.receitix.DTO.response.ProdutoDTOResponse;
import com.yasmin.receitix.DTO.response.ProdutoDTOUpdateResponse;
import com.yasmin.receitix.entity.Produto;
import com.yasmin.receitix.repository.CategoriaRepository;
import com.yasmin.receitix.repository.ProdutoRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
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

        // Decodifica a string Base64 para byte[] antes de salvar
        if (produtoDTORequest.getImagemBase64() != null && !produtoDTORequest.getImagemBase64().isEmpty()) {
            // Remove o prefixo Data URI (ex: data:image/jpeg;base64,)
            String base64Image = produtoDTORequest.getImagemBase64().split(",")[1];
            byte[] imagemBytes = Base64.getDecoder().decode(base64Image);
            produto.setImagem(imagemBytes);
        } else {
            produto.setImagem(null);
        }

        produto.setNome(produtoDTORequest.getNome());
        produto.setPreco(produtoDTORequest.getPreco());
        produto.setCategoria(categoriaRepository.obterCategoriaPeloId(produtoDTORequest.getIdCategoria()));
        Produto produtoSave = this.produtoRepository.save(produto);
        ProdutoDTOResponse produtoDTOResponse = modelMapper.map(produtoSave, ProdutoDTOResponse.class);
        return produtoDTOResponse;
    }

    public ProdutoDTOResponse atualizarProduto(@Valid Integer idProduto, ProdutoDTORequest produtoDTORequest) {
        Produto produto = this.listarPorProdutoId(idProduto);

        if (produto != null){
            produto.setCriado(produtoDTORequest.getCriado());
            produto.setDescricao(produtoDTORequest.getDescricao());
            produto.setStatus(produtoDTORequest.getStatus());

            // Decodifica a string Base64 para byte[] antes de salvar
            if (produtoDTORequest.getImagemBase64() != null && !produtoDTORequest.getImagemBase64().isEmpty()) {
                // Remove o prefixo Data URI (ex: data:image/jpeg;base64,)
                String base64Image = produtoDTORequest.getImagemBase64().split(",")[1];
                byte[] imagemBytes = Base64.getDecoder().decode(base64Image);
                produto.setImagem(imagemBytes);
            } else {
                produto.setImagem(produto.getImagem()); // Mantém a imagem existente se não for enviada uma nova
            }

            produto.setNome(produtoDTORequest.getNome());
            produto.setPreco(produtoDTORequest.getPreco());
            produto.setCategoria(categoriaRepository.obterCategoriaPeloId(produtoDTORequest.getIdCategoria()));
            Produto produtoTemp = this.produtoRepository.save(produto);
            return modelMapper.map(produtoTemp, ProdutoDTOResponse.class);
        }else {
            return null;
        }

    }

    public ProdutoDTOUpdateResponse atualizarStatusProduto(Integer produtoId, ProdutoDTOUpdateRequest produtoDTOUpdateRequest) {
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