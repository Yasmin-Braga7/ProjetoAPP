package com.Yasmin.Receitix.controllers;

import com.Yasmin.Receitix.entity.PedidoItem;
import com.Yasmin.Receitix.service.PedidoItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @DeleteMapping("/apagar/{pedidoItemId}")
    @Operation(summary = "Apagar item de pedido", description = "Endpoint para apagar um item de pedido pelo id")
    public  ResponseEntity apagarPedidoItem(@PathVariable("pedidoItemId") Integer pedidoItemId){
        pedidoItemService.apagarPedidoItem(pedidoItemId);
        return ResponseEntity.noContent().build();
    }
}

