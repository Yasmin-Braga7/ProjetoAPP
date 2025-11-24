package com.Yasmin.Receitix.service;

import com.Yasmin.Receitix.DTO.LoginUserDTO;
import com.Yasmin.Receitix.DTO.RecoveryJwtTokenDTO;
import com.Yasmin.Receitix.DTO.request.UsuarioDTORequest;
import com.Yasmin.Receitix.DTO.request.UsuarioDTOUpdateRequest;
import com.Yasmin.Receitix.DTO.response.UsuarioDTOResponse;
import com.Yasmin.Receitix.DTO.response.UsuarioDTOUpdateResponse;
import com.Yasmin.Receitix.config.SecurityConfiguration;
import com.Yasmin.Receitix.entity.Role;
import com.Yasmin.Receitix.entity.RoleName;
import com.Yasmin.Receitix.entity.Usuario;
import com.Yasmin.Receitix.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    private final RoleService roleService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private ModelMapper modelMapper;


    public UsuarioService(UsuarioRepository usuarioRepository, RoleService roleService) {
        this.usuarioRepository = usuarioRepository;
        this.roleService = roleService;
    }

    // Método responsável por autenticar um usuário e retornar um token JWT
    public RecoveryJwtTokenDTO authenticateUser(LoginUserDTO loginUserDTO) {
        // Cria um objeto de autenticação com o email e a senha do usuário
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDTO.email(), loginUserDTO.senha());

        // Autentica o usuário com as credenciais fornecidas
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // Obtém o objeto UserDetails do usuário autenticado
        UsuarioDetailsImpl usuarioDetails = (UsuarioDetailsImpl) authentication.getPrincipal();

        RecoveryJwtTokenDTO recoveryJwtTokenDTO = new RecoveryJwtTokenDTO();

        UsuarioDTOResponse usuarioDTOResponse = new UsuarioDTOResponse();
        Usuario usuario = usuarioRepository.findByEmail(loginUserDTO.email());
        usuarioDTOResponse.setId(usuario.getId());
        usuarioDTOResponse.setNome(usuario.getNome());
        usuarioDTOResponse.setEmail(usuario.getEmail());
        usuarioDTOResponse.setTelefone(usuario.getTelefone());
        usuarioDTOResponse.setEndereco(usuario.getEndereco());
        usuarioDTOResponse.setStatus(usuario.getStatus());

        recoveryJwtTokenDTO.setUsuario(usuarioDTOResponse);
        recoveryJwtTokenDTO.setToken(jwtTokenService.generateToken(usuarioDetails));

        // Gera um token JWT para o usuário autenticado
        return recoveryJwtTokenDTO;
    }


    public List<Usuario> listarUsuarios(){
        return this.usuarioRepository.listarUsuarios();
    }

    public Usuario listarPorUsuarioId(Integer usuarioId) {
        return this.usuarioRepository.obterUsuarioPeloId(usuarioId);
    }

    public UsuarioDTOResponse criarUsuario(UsuarioDTORequest usuarioDTORequest) {

        Usuario usuario = new Usuario();

        Role role;
        RoleName roleNome = usuarioDTORequest.getRole();
        role = roleService.getRolesByName(roleNome);
        usuario.setRoles(List.of(role));

        usuario.setNome(usuarioDTORequest.getNome());
        usuario.setEmail(usuarioDTORequest.getEmail());
        usuario.setTelefone(usuarioDTORequest.getTelefone());
        usuario.setEndereco(usuarioDTORequest.getEndereco());
        usuario.setSenha(securityConfiguration.passwordEncoder().encode(usuarioDTORequest.getSenha()));
        usuario.setCriado(usuarioDTORequest.getCriado());
        usuario.setStatus(usuarioDTORequest.getStatus());
        usuario.setRoles(List.of(role));
        Usuario usuarioSave = this.usuarioRepository.save(usuario);
        UsuarioDTOResponse usuarioDTOResponse = modelMapper.map(usuarioSave, UsuarioDTOResponse.class);
        return usuarioDTOResponse;
    }

    public UsuarioDTOResponse atualizarUsuario(Integer usuarioId, UsuarioDTORequest usuarioDTORequest) {
        //antes de atualizar busca se existe o registro a ser atualizar
        Usuario usuario = this.listarPorUsuarioId(usuarioId);

        //se encontra o registro a ser atualizado
        if (usuario != null){
            usuario.setNome(usuarioDTORequest.getNome());
            usuario.setEmail(usuarioDTORequest.getEmail());
            usuario.setTelefone(usuarioDTORequest.getTelefone());
            usuario.setEndereco(usuarioDTORequest.getEndereco());
            usuario.setSenha(securityConfiguration.passwordEncoder().encode(usuarioDTORequest.getSenha()));
            usuario.setCriado(usuarioDTORequest.getCriado());
            usuario.setStatus(usuarioDTORequest.getStatus());

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
