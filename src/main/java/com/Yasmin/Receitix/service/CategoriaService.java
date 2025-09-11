package com.Yasmin.Receitix.service;

import com.Yasmin.Receitix.DTO.request.CategoriaDTORequest;
import com.Yasmin.Receitix.DTO.request.CategoriaDTOUpdateRequest;
import com.Yasmin.Receitix.DTO.response.CategoriaDTOResponse;
import com.Yasmin.Receitix.DTO.response.CategoriaDTOUpdateResponse;
import com.Yasmin.Receitix.entity.Categoria;
import com.Yasmin.Receitix.repository.CategoriaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    @Autowired
    private ModelMapper modelMapper;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listarCategorias(){
        return this.categoriaRepository.listarCategorias();
    }

    public Categoria listarPorCategoriaId(Integer categoriasId) {
        return this.categoriaRepository.obterCategoriaPeloId(categoriasId);
    }

    public CategoriaDTOResponse criarCategoria(CategoriaDTORequest categoriaDTORequest) {

        Categoria categoria = modelMapper.map(categoriaDTORequest, Categoria.class);
        Categoria categoriaSave = this.categoriaRepository.save(categoria);
        CategoriaDTOResponse categoriaDTOResponse = modelMapper.map(categoriaSave, CategoriaDTOResponse.class);
        return categoriaDTOResponse;
    }

    public CategoriaDTOResponse atualizarCategoria(Integer categoriaId, CategoriaDTORequest categoriaDTORequest) {
        //antes de atualizar busca se existe o registro a ser atualizar
        Categoria categoria = this.listarPorCategoriaId(categoriaId);

        //se encontra o registro a ser atualizado
        if (categoria != null){
            //copia os dados a serem atualizados do DTO de entrada para um objeto do tipo participante
            //que é compatível com o repository para atualizar
            modelMapper.map(categoriaDTORequest,categoria);

            //com o objeto no formato correto tipo "participante" o comando "save" salva
            // no banco de dados o objeto atualizado
            Categoria tempResponse = categoriaRepository.save(categoria);

            return modelMapper.map(tempResponse, CategoriaDTOResponse.class);
        }else {
            return null;
        }

    }

    public CategoriaDTOUpdateResponse atualizarStatusCategoria(Integer categoriaId, CategoriaDTOUpdateRequest categoriaDTOUpdateRequest) {
        //antes de atualizar busca se existe o registro a ser atualizar
        Categoria categoria = this.listarPorCategoriaId(categoriaId);

        //se encontra o registro a ser atualizado
        if (categoria != null) {
            //atualizamos unicamente o campo de status
            categoria.setStatus(categoriaDTOUpdateRequest.getStatus());

            //com o objeto no formato correto tipo "participante" o comando "save" salva
            // no banco de dados o objeto atualizado
            Categoria tempResponse = categoriaRepository.save(categoria);
            return modelMapper.map(tempResponse, CategoriaDTOUpdateResponse.class);
        }
        else{
            return null;
        }
    }

    public void apagarCategoria(Integer categoriaId){
        categoriaRepository.apagadoLogicoCategoria(categoriaId);
    }
}
