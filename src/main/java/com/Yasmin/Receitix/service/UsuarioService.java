package com.Yasmin.Receitix.service;

import com.Yasmin.Receitix.DTO.request.UsuarioDTORequest;
import com.Yasmin.Receitix.DTO.request.UsuarioDTOUpdateRequest;
import com.Yasmin.Receitix.DTO.response.UsuarioDTOResponse;
import com.Yasmin.Receitix.DTO.response.UsuarioDTOUpdateResponse;
import com.Yasmin.Receitix.entity.Usuario;
import com.Yasmin.Receitix.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    @Autowired
    private ModelMapper modelMapper;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarUsuarios(){
        return this.usuarioRepository.listarUsuarios();
    }

    public Usuario listarPorUsuarioId(Integer usuarioId) {
        return this.usuarioRepository.obterUsuarioPeloId(usuarioId);
    }

    public UsuarioDTOResponse criarUsuario(UsuarioDTORequest usuarioDTORequest) {

        Usuario usuario = modelMapper.map(usuarioDTORequest, Usuario.class);
        Usuario usuarioSave = this.usuarioRepository.save(usuario);
        UsuarioDTOResponse usuarioDTOResponse = modelMapper.map(usuarioSave, UsuarioDTOResponse.class);
        return usuarioDTOResponse;
    }

    public UsuarioDTOResponse atualizarUsuario(Integer usuarioId, UsuarioDTORequest usuarioDTORequest) {
        //antes de atualizar busca se existe o registro a ser atualizar
        Usuario usuario = this.listarPorUsuarioId(usuarioId);

        //se encontra o registro a ser atualizado
        if (usuario != null){
            //copia os dados a serem atualizados do DTO de entrada para um objeto do tipo participante
            //que é compatível com o repository para atualizar
            modelMapper.map(usuarioDTORequest,usuario);

            //com o objeto no formato correto tipo "participante" o comando "save" salva
            // no banco de dados o objeto atualizado
            Usuario tempResponse = usuarioRepository.save(usuario);

            return modelMapper.map(tempResponse, UsuarioDTOResponse.class);
        }else {
            return null;
        }

    }

    public UsuarioDTOUpdateResponse atualizarStatusUsuario(Integer usuarioId, UsuarioDTOUpdateRequest usuarioDTOUpdateRequest) {
        //antes de atualizar busca se existe o registro a ser atualizar
        Usuario usuario = this.listarPorUsuarioId(usuarioId);

        //se encontra o registro a ser atualizado
        if (usuario != null) {
            //atualizamos unicamente o campo de status
            usuario.setStatus(usuarioDTOUpdateRequest.getStatus());

            //com o objeto no formato correto tipo "participante" o comando "save" salva
            // no banco de dados o objeto atualizado
            Usuario tempResponse = usuarioRepository.save(usuario);
            return modelMapper.map(tempResponse, UsuarioDTOUpdateResponse.class);
        }
        else{
            return null;
        }
    }

    public void apagarUsuario(Integer usuarioId){
        usuarioRepository.apagadoLogicoUsuario(usuarioId);
    }
}
