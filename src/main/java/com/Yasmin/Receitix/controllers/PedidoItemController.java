package com.Yasmin.Receitix.controllers;


import com.Yasmin.Receitix.DTO.request.PedidoItemDTORequest;
import com.Yasmin.Receitix.DTO.request.PedidoItemDTOUpdateRequest;
import com.Yasmin.Receitix.DTO.response.PedidoItemDTOResponse;
import com.Yasmin.Receitix.DTO.response.PedidoItemDTOUpdateResponse;
import com.Yasmin.Receitix.entity.PedidoItem;
import com.Yasmin.Receitix.service.PedidoItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/pedidoItem")
@Tag(name="PedidoItem", description="API para gerenciamento de pedidoItem")
public class PedidoItemController {
    private PedidoItemService pedidoItemService;

    public PedidoItemController(PedidoItemService pedidoItemService) {
        this.pedidoItemService = pedidoItemService;
    }

    @GetMapping("/listar")
    @Operation(summary="Listar pedidoItem", description = "Endpoint para listar todos os pedidosItens")
    public ResponseEntity<List<PedidoItem>> listarPedidosItens(){
        return ResponseEntity.ok(pedidoItemService.listarPedidosItens());
    }

    @GetMapping("/listarPorPedidoItemId/{pedidoItemId}")
    @Operation(summary = "Listar pedidoItem pelo id de pedidoItem", description = "Endpoint para obter pedidoItem pelo id de pedidoItem")
    public ResponseEntity<PedidoItem> listarPorPedidoItemId(@PathVariable("pedidoItemId") Integer pedidoItemId){
        PedidoItem pedidoItem = pedidoItemService.listarPorPedidoItemId(pedidoItemId);
        if(pedidoItem == null) {
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok(pedidoItem);
        }
    }

    @PostMapping("/criar")
    @Operation(summary = "Criar novo pedidoItem", description = "Endpoint para criar um novo registro de pedidoItem")
    public ResponseEntity<PedidoItemDTOResponse> criarPedidoItem(
            @Valid @RequestBody PedidoItemDTORequest pedidoItem){
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoItemService.criarPedidoItem(pedidoItem));
    }

    @PutMapping("/atualizar/{pedidoItemId}")
    @Operation(summary = "Atualizar todos dados do pedidoItem", description = "Endpoint para atualizar o registro de pedidoItem")
    public ResponseEntity<PedidoItemDTOResponse> atualizarPedidoItem(
            @PathVariable("pedidoItemId") Integer pedidoItemId,
            @RequestBody PedidoItemDTORequest pedidoItemDTORequest){


        return ResponseEntity.ok(pedidoItemService.atualizarPedidoItem(pedidoItemId, pedidoItemDTORequest));
    }

    @PatchMapping("/atualizarStatus/{pedidoItemId}")
    @Operation(summary = "Atualizar campo status do pedidoItem", description = "Endpoint para atualizar o status do pedidoItem")
    public ResponseEntity<PedidoItemDTOUpdateResponse> atualizarStatusPedidoItem(
            @Valid
            @PathVariable("pedidoItemId") Integer pedidoItemId,
            @RequestBody PedidoItemDTOUpdateRequest pedidoItemDTOUpdateRequest){
        return  ResponseEntity.ok(pedidoItemService.atualizarStatusPedidoItem(pedidoItemId, pedidoItemDTOUpdateRequest));
    }

    @DeleteMapping("/apagar/{pedidoItemId}")
    @Operation(summary = "Apagar registro de pedidoItem", description = "Endpoint para apagar um pedidoItem pelo id")
    public  ResponseEntity apagarPedidoItem(@PathVariable("pedidoItemId") Integer pedidoItemId){
        pedidoItemService.apagarPedidoItem(pedidoItemId);
        return ResponseEntity.noContent().build();
    }
}
