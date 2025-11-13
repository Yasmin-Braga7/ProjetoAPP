package com.yasmin.receitix.controllers;

import com.yasmin.receitix.DTO.request.PedidoItemDTORequest;
import com.yasmin.receitix.DTO.response.PedidoItemDTOResponse;
import com.yasmin.receitix.entity.PedidoItem;
import com.yasmin.receitix.service.PedidoItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/pedidoItem")
@Tag(name="PedidoItem", description="API para gerenciamento de itens de pedido")
public class PedidoItemController {

    private PedidoItemService pedidoItemService;

    public PedidoItemController(PedidoItemService pedidoItemService) {
        this.pedidoItemService = pedidoItemService;
    }

    @GetMapping("/listar")
    @Operation(summary="Listar itens de pedido", description = "Endpoint para listar todos os itens de pedido")
    public ResponseEntity<List<PedidoItem>> listarPedidoItens(){
        return ResponseEntity.ok(pedidoItemService.listarPedidoItens());
    }

    @GetMapping("/listarPorId/{pedidoItemId}")
    @Operation(summary = "Listar item de pedido pelo id", description = "Endpoint para obter item de pedido pelo id")
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

    @DeleteMapping("/apagar/{pedidoItemId}")
    @Operation(summary = "Apagar item de pedido", description = "Endpoint para apagar um item de pedido pelo id")
    public  ResponseEntity apagarPedidoItem(@PathVariable("pedidoItemId") Integer pedidoItemId){
        pedidoItemService.apagarPedidoItem(pedidoItemId);
        return ResponseEntity.noContent().build();
    }
}

