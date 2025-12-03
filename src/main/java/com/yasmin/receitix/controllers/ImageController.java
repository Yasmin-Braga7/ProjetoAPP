package com.yasmin.receitix.controllers;

import com.yasmin.receitix.DTO.response.ImageBase64Response;
import com.yasmin.receitix.entity.Produto;
import com.yasmin.receitix.service.ImageService;
import com.yasmin.receitix.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping("/api/v1/images")
@CrossOrigin("*")
public class ImageController {
    private final ProdutoService produtoService;
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService, ProdutoService produtoService) {
        this.imageService = imageService;
        this.produtoService = produtoService;
    }

    @GetMapping("/foto/{idProdutoFoto}")
    @Operation(summary = "Obter foto de um produto pelo id da foto", description = "Get foto de um produto pelo id da foto")
    public ResponseEntity<byte[]> exibirFoto(@PathVariable("idProdutoFoto") Integer idProdutoFoto) {
        Produto produtoFoto = produtoService.listarPorProdutoId(idProdutoFoto);

        byte[] dados = produtoFoto.getImagem();
        if (dados == null || dados.length == 0) {
            return ResponseEntity.notFound().build();
        }

        String extensao = produtoFoto.getExtensao().toLowerCase();
        MediaType mediaType = switch (extensao) {
            case "jpeg", "jpg" -> MediaType.IMAGE_JPEG;
            case "png" -> MediaType.IMAGE_PNG;
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setContentDisposition(ContentDisposition.inline()
                .filename(produtoFoto.getImagemNome())
                .build());

        // Decodifica o base64 de string para bytes
        String base64 = new String(dados);
        String base64SemPrefixo = base64.substring(base64.indexOf(',') + 1);
        byte[] imagem = Base64.getDecoder().decode(base64SemPrefixo);

        return new ResponseEntity<>(imagem, headers, HttpStatus.OK);
    }

    @PostMapping("/foto/upload/{idProduto}")
    @Operation(summary = "Fazer upload de foto de um produto", description = "Envia uma imagem e a associa a um produto")
    public ResponseEntity<Void> uploadFoto(@PathVariable("idProduto") Integer idProduto, @RequestParam("file") MultipartFile file) {


        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Arquivo está vazio");
        }

        try {
            // Lê os bytes da imagem
            byte[] bytesImagem = file.getBytes();

            // Detecta o tipo do arquivo (extensão/mime)
            String contentType = file.getContentType(); // Ex: image/jpeg
            String extensao = switch (contentType) {
                case "image/jpeg", "image/jpg" -> "jpg";
                case "image/png" -> "png";
                default -> throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Formato de imagem não suportado");
            };

            // Codifica a imagem em base64 com prefixo data URI (se for necessário)
            String base64 = Base64.getEncoder().encodeToString(bytesImagem);
            String prefixo = "data:" + contentType + ";base64,";
            String base64ComPrefixo = prefixo + base64;

            // Cria e salva a entidade
            Produto produtoFoto = produtoService.listarPorProdutoId(idProduto);
            if (produtoFoto==null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O produto com id:"+ idProduto + " não existe");
            }
            produtoFoto.setImagem(base64ComPrefixo.getBytes(StandardCharsets.UTF_8));
            produtoFoto.setExtensao(extensao);
            produtoFoto.setImagemNome(file.getOriginalFilename());

            produtoService.atualizarProdutoFoto(produtoFoto);

            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao processar o arquivo", e);
        }
    }

    @DeleteMapping("/{idProdutoFoto}")
    @Operation(summary = "Excluir a foto de um produto", description = "Exclui uma foto do produto existente pelo ID")
    public ResponseEntity<Void> excluirProdutoFotos(@PathVariable("idProdutoFoto") Integer id) {
        produtoService.apagarProduto(id);
        return ResponseEntity.noContent().build();
    }

}