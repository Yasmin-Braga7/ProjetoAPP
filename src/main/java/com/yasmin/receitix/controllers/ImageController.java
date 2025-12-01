package com.yasmin.receitix.controllers;

import com.yasmin.receitix.DTO.response.ImageBase64Response;
import com.yasmin.receitix.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/to-base64")
    public ResponseEntity<ImageBase64Response> convertImageToBase64(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            byte[] bytes = file.getBytes();
            String base64Image = imageService.convertImageToBase64(bytes);
            String contentType = file.getContentType();

            // Adiciona o prefixo Data URI para facilitar o uso no frontend
            String base64WithPrefix = imageService.addDataUriPrefix(base64Image, contentType);

            ImageBase64Response response = new ImageBase64Response(base64WithPrefix, contentType);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}