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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
        // 1. Cria o objeto de autenticação
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDTO.email(), loginUserDTO.senha());

        // 2. Autentica (Se falhar, o Spring Security lança erro aqui)
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // 3. Pega os detalhes do usuário autenticado
        UsuarioDetailsImpl usuarioDetails = (UsuarioDetailsImpl) authentication.getPrincipal();

        // 4. Prepara o objeto de resposta (Token + Dados do Usuário)
        RecoveryJwtTokenDTO recoveryJwtTokenDTO = new RecoveryJwtTokenDTO();

        UsuarioDTOResponse usuarioDTOResponse = new UsuarioDTOResponse();

        // Busca o usuário completo no banco para pegar todos os dados
        Usuario usuario = usuarioRepository.findByEmail(loginUserDTO.email());

        // --- Mapeamento Manual dos Dados ---
        usuarioDTOResponse.setId(usuario.getId());
        usuarioDTOResponse.setNome(usuario.getNome());
        usuarioDTOResponse.setEmail(usuario.getEmail());
        usuarioDTOResponse.setTelefone(usuario.getTelefone());
        usuarioDTOResponse.setEndereco(usuario.getEndereco());
        usuarioDTOResponse.setStatus(usuario.getStatus());

        // Retorna a imagem para o app já carregar a foto de perfil
        usuarioDTOResponse.setImagem(usuario.getImagem());

        // [NOVO] Converte as Roles (Cargos) para uma lista de Strings (ex: "ROLE_ADMINISTRADOR")
        // Isso é essencial para o redirecionamento no Frontend!
        List<String> roles = usuario.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());
        usuarioDTOResponse.setRoles(roles);

        recoveryJwtTokenDTO.setUsuario(usuarioDTOResponse);
        recoveryJwtTokenDTO.setToken(jwtTokenService.generateToken(usuarioDetails));

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

        // Se vier imagem na criação
        if(usuarioDTORequest.getImagem() != null){
            usuario.setImagem(usuarioDTORequest.getImagem());
        }

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

            // Verifica se a senha foi enviada antes de reencodar (evita mudar senha sem querer)
            if (usuarioDTORequest.getSenha() != null && !usuarioDTORequest.getSenha().isEmpty()) {
                usuario.setSenha(securityConfiguration.passwordEncoder().encode(usuarioDTORequest.getSenha()));
            }

            usuario.setCriado(usuarioDTORequest.getCriado());
            usuario.setStatus(usuarioDTORequest.getStatus());

            Usuario tempResponse = usuarioRepository.save(usuario);

            return modelMapper.map(tempResponse, UsuarioDTOResponse.class);
        }else {
            return null;
        }
    }

    // Parte da imagem de usuario !!

    public UsuarioDTOResponse atualizarFotoPerfil(Integer usuarioId, MultipartFile arquivo) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + usuarioId));

        try {
            //
            usuario.setImagem(arquivo.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar a imagem do perfil: " + e.getMessage());
        }

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return modelMapper.map(usuarioSalvo, UsuarioDTOResponse.class);
    }

    public void removerFotoPerfil(Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + usuarioId));

        usuario.setImagem(null);
        usuarioRepository.save(usuario);
    }


    public UsuarioDTOUpdateResponse atualizarStatusUsuario(Integer usuarioId, UsuarioDTOUpdateRequest usuarioDTOUpdateRequest) {
        Usuario usuario = this.listarPorUsuarioId(usuarioId);

        if (usuario != null) {
            usuario.setStatus(usuarioDTOUpdateRequest.getStatus());
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