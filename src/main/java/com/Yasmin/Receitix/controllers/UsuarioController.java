package com.Yasmin.Receitix.controllers;

import com.Yasmin.Receitix.DTO.request.UsuarioDTORequest;
import com.Yasmin.Receitix.DTO.request.UsuarioDTOUpdateRequest;
import com.Yasmin.Receitix.DTO.response.UsuarioDTOResponse;
import com.Yasmin.Receitix.DTO.response.UsuarioDTOUpdateResponse;
import com.Yasmin.Receitix.entity.Usuario;
import com.Yasmin.Receitix.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/participante")
@Tag(name="Participante", description="API para gerenciamento de participantes")
public class UsuarioController {
    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
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
}
