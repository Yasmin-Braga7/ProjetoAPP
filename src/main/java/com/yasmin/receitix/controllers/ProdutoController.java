package com.yasmin.receitix.controllers;


import com.yasmin.receitix.DTO.request.ProdutoDTORequest;
import com.yasmin.receitix.DTO.request.ProdutoDTOUpdateRequest;
import com.yasmin.receitix.DTO.response.ProdutoDTOResponse;
import com.yasmin.receitix.DTO.response.ProdutoDTOUpdateResponse;
import com.yasmin.receitix.entity.Produto;
import com.yasmin.receitix.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/produto")
@Tag(name="Produto", description="API para gerenciamento de produto")
public class ProdutoController {
    private ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping("/listar")
    @Operation(summary="Listar produtos", description = "Endpoint para listar todos os produtos")
    public ResponseEntity<List<Produto>> listarProdutos(){
        return ResponseEntity.ok(produtoService.listarProdutos());
    }

    @GetMapping("/listarPorProdutoId/{produtoId}")
    @Operation(summary = "Listar produto pelo id de produto", description = "Endpoint para obter participante pelo id de participante")
    public ResponseEntity<Produto> listarPorProdutoId(@PathVariable("produtoId") Integer produtoId){
        Produto produto = produtoService.listarPorProdutoId(produtoId);
        if(produto == null) {
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok(produto);
        }
    }

    @PostMapping("/criar")
    @Operation(summary = "Criar novo produto", description = "Endpoint para criar um novo registro de produto")
    public ResponseEntity<ProdutoDTOResponse> criarProduto(
            @Valid @RequestBody ProdutoDTORequest produto){
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.criarProduto(produto));
    }

    @PutMapping("/atualizar/{produtoId}")
    @Operation(summary = "Atualizar todos dados do produto", description = "Endpoint para atualizar o registro de produto")
    public ResponseEntity<ProdutoDTOResponse> atualizarProduto(
            @PathVariable("produtoId") Integer produtoId,
            @RequestBody ProdutoDTORequest produtoDTORequest){


        return ResponseEntity.ok(produtoService.atualizarProduto(produtoId, produtoDTORequest));
    }

    @PatchMapping("/atualizarStatus/{produtoId}")
    @Operation(summary = "Atualizar campo status do produto", description = "Endpoint para atualizar o status do produto")
    public ResponseEntity<ProdutoDTOUpdateResponse> atualizarStatusProduto(
            @Valid
            @PathVariable("produtoId") Integer produtoId,
            @RequestBody ProdutoDTOUpdateRequest produtoDTOUpdateRequest){
        return  ResponseEntity.ok(produtoService.atualizarStatusProduto(produtoId, produtoDTOUpdateRequest));
    }

    @DeleteMapping("/apagar/{produtoId}")
    @Operation(summary = "Apagar registro de produto", description = "Endpoint para apagar um produto pelo id")
    public  ResponseEntity apagarProduto(@PathVariable("produtoId") Integer produtoId){
        produtoService.apagarProduto(produtoId);
        return ResponseEntity.noContent().build();
    }
}
