package com.yasmin.receitix.service;

import com.yasmin.receitix.DTO.LoginUserDTO;
import com.yasmin.receitix.DTO.RecoveryJwtTokenDTO;
import com.yasmin.receitix.DTO.request.UsuarioDTORequest;
import com.yasmin.receitix.DTO.request.UsuarioDTOUpdateRequest;
import com.yasmin.receitix.DTO.response.UsuarioDTOResponse;
import com.yasmin.receitix.DTO.response.UsuarioDTOUpdateResponse;
import com.yasmin.receitix.config.SecurityConfiguration;
import com.yasmin.receitix.entity.Role;
import com.yasmin.receitix.entity.RoleName;
import com.yasmin.receitix.entity.Usuario;
import com.yasmin.receitix.repository.UsuarioRepository;
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

        RoleName roleNome = usuarioDTORequest.getRole();
        if (roleNome == null) {
            roleNome = RoleName.ROLE_CLIENTE;
        }
        Role role = roleService.getRolesByName(roleNome);
        if (role == null) {
            throw new RuntimeException("Erro: Role " + roleNome + " não encontrada no banco de dados.");
        }
        usuario.setRoles(List.of(role));

        usuario.setNome(usuarioDTORequest.getNome());
        usuario.setEmail(usuarioDTORequest.getEmail());
        usuario.setTelefone(usuarioDTORequest.getTelefone());
        usuario.setEndereco(usuarioDTORequest.getEndereco());
        usuario.setSenha(securityConfiguration.passwordEncoder().encode(usuarioDTORequest.getSenha()));
        if (usuarioDTORequest.getCriado() != null) {
            usuario.setCriado(usuarioDTORequest.getCriado());
        } else {
            usuario.setCriado(java.time.LocalDateTime.now());
        }
        usuario.setStatus(1);
        Usuario usuarioSave = this.usuarioRepository.save(usuario);
        UsuarioDTOResponse usuarioDTOResponse = modelMapper.map(usuarioSave, UsuarioDTOResponse.class);
        return usuarioDTOResponse;
    }

    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
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
