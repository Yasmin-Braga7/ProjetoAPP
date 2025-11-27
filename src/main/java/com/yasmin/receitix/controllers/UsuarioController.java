package com.yasmin.receitix.controllers;

import com.yasmin.receitix.DTO.LoginUserDTO;
import com.yasmin.receitix.DTO.RecoveryJwtTokenDTO;
import com.yasmin.receitix.DTO.request.UsuarioDTORequest;
import com.yasmin.receitix.DTO.request.UsuarioDTOUpdateRequest;
import com.yasmin.receitix.DTO.response.UsuarioDTOResponse;
import com.yasmin.receitix.DTO.response.UsuarioDTOUpdateResponse;
import com.yasmin.receitix.entity.Usuario;
import com.yasmin.receitix.repository.UsuarioRepository;
import com.yasmin.receitix.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/usuario")
@Tag(name="Usuario", description="API para gerenciamento de usuarios")
public class UsuarioController {
    private UsuarioService usuarioService;

    private UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioService usuarioService, UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/listar")
    @Operation(summary="Listar usuarios", description = "Endpoint para listar todos os usuarios")
    public ResponseEntity<List<Usuario>> listarParticipantes(){
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @GetMapping("/listarPorUsuarioId/{usuarioId}")
    @Operation(summary = "Listar usuario pelo id de usuario", description = "Endpoint para obter usuario pelo id de usuario")
    public ResponseEntity<Usuario> listarPorUsuarioId(@PathVariable("usuarioId") Integer usuarioId){
        Usuario usuario = usuarioService.listarPorUsuarioId(usuarioId);
        if(usuario == null) {
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok(usuario);
        }
    }

    @PostMapping("/criar")
    @Operation(summary = "Criar novo usuario", description = "Endpoint para criar um novo registro de usuario")
    public ResponseEntity<UsuarioDTOResponse> criarUsuario(
            @Valid @RequestBody UsuarioDTORequest usuario){
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.criarUsuario(usuario));
    }

    @PutMapping("/atualizar/{usuarioId}")
    @Operation(summary = "Atualizar todos dados do usuario", description = "Endpoint para atualizar o registro de usuario")
    public ResponseEntity<UsuarioDTOResponse> atualizarUsuario(
            @PathVariable("usuarioId") Integer usuarioId,
            @RequestBody UsuarioDTORequest usuarioDTORequest){


        return ResponseEntity.ok(usuarioService.atualizarUsuario(usuarioId, usuarioDTORequest));
    }

    @PostMapping("/uploadImagem/{usuarioId}")
    public ResponseEntity<String> uploadImagem(
            @PathVariable Integer usuarioId,
            @RequestParam("imagem") MultipartFile file
    ) {
        try {
            Usuario usuario = usuarioService.listarPorUsuarioId(usuarioId);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
            }

            usuario.setImagem(file.getBytes());
            usuarioService.salvar(usuario); // EXPLICO LOGO ABAIXO

            return ResponseEntity.ok("Imagem enviada com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao enviar imagem");
        }
    }



    @PatchMapping("/atualizarStatus/{usuarioId}")
    @Operation(summary = "Atualizar campo status do usuario", description = "Endpoint para atualizar o status do usuario")
    public ResponseEntity<UsuarioDTOUpdateResponse> atualizarStatusUsuario(
            @Valid
            @PathVariable("usuarioId") Integer usuarioId,
            @RequestBody UsuarioDTOUpdateRequest usuarioDTOUpdateRequest){
        return  ResponseEntity.ok(usuarioService.atualizarStatusUsuario(usuarioId, usuarioDTOUpdateRequest));
    }

    @DeleteMapping("/apagar/{usuarioId}")
    @Operation(summary = "Apagar registro de usuario", description = "Endpoint para apagar um usuario pelo id")
    public  ResponseEntity apagarUsuario(@PathVariable("usuarioId") Integer usuarioId){
        usuarioService.apagarUsuario(usuarioId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<RecoveryJwtTokenDTO> authenticateUser(@RequestBody LoginUserDTO loginUserDTO) {
        RecoveryJwtTokenDTO token = usuarioService.authenticateUser(loginUserDTO);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
