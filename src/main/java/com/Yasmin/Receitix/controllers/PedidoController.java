package com.Yasmin.Receitix.controllers;

import com.Yasmin.Receitix.DTO.request.PedidoDTORequest;
import com.Yasmin.Receitix.DTO.request.PedidoDTOUpdateRequest;
import com.Yasmin.Receitix.DTO.response.PedidoDTOResponse;
import com.Yasmin.Receitix.DTO.response.PedidoDTOUpdateResponse;
import com.Yasmin.Receitix.entity.Pedido;
import com.Yasmin.Receitix.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/pedido")
@Tag(name="Pedido", description="API para gerenciamento de pedido")
public class PedidoController {
    private PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/listar")
    @Operation(summary="Listar pedidos", description = "Endpoint para listar todos os pedidos")
    public ResponseEntity<List<Pedido>> listarPedidos(){
        return ResponseEntity.ok(pedidoService.listarPedidos());
    }

    @GetMapping("/listarPorPedidoId/{pedidoId}")
    @Operation(summary = "Listar participante pelo id de participante", description = "Endpoint para obter participante pelo id de participante")
    public ResponseEntity<Pedido> listarPorPedidoId(@PathVariable("pedidoId") Integer pedidoId){
        Pedido pedido = pedidoService.listarPorPedidoId(pedidoId);
        if(pedido == null) {
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok(pedido);
        }
    }

    @PostMapping("/criar")
    @Operation(summary = "Criar novo participante", description = "Endpoint para criar um novo registro de participante")
    public ResponseEntity<PedidoDTOResponse> criarPedido(
            @Valid @RequestBody PedidoDTORequest pedido){
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.criarPedido(pedido));
    }

    @PutMapping("/atualizar/{pedidoId}")
    @Operation(summary = "Atualizar todos dados do participante", description = "Endpoint para atualizar o registro de participante")
    public ResponseEntity<PedidoDTOResponse> atualizarPedido(
            @PathVariable("participanteId") Integer pedidoId,
            @RequestBody PedidoDTORequest pedidoDTORequest){


        return ResponseEntity.ok(pedidoService.atualizarPedido(pedidoId, pedidoDTORequest));
    }

    @PatchMapping("/atualizarStatus/{pedidoId}")
    @Operation(summary = "Atualizar campo status do pedido", description = "Endpoint para atualizar o status do pedido")
    public ResponseEntity<PedidoDTOUpdateResponse> atualizarStatusPedido(
            @Valid
            @PathVariable("pedidoId") Integer pedidoId,
            @RequestBody PedidoDTOUpdateRequest pedidoDTOUpdateRequest){
        return  ResponseEntity.ok(pedidoService.atualizarStatusPedido(pedidoId, pedidoDTOUpdateRequest));
    }

    @DeleteMapping("/apagar/{pedidoId}")
    @Operation(summary = "Apagar registro de pedido", description = "Endpoint para apagar um pedido pelo id")
    public  ResponseEntity apagarPedido(@PathVariable("pedidoId") Integer pedidoId){
        pedidoService.apagarPedido(pedidoId);
        return ResponseEntity.noContent().build();
    }
}
