package com.yasmin.receitix.DTO.response;

public class ImageBase64Response {
    private String base64Image;
    private String contentType;

    public ImageBase64Response(String base64Image, String contentType) {
        this.base64Image = base64Image;
        this.contentType = contentType;
    }

    // Getters and Setters
    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}