package com.ecommerece.store.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ImageDto {
    private int imageId;
    private String imageName;
    private String downloadUrl;

    public ImageDto(int imageId, String imageName, String downloadUrl) {
        this.imageId = imageId;
        this.imageName = imageName;
        this.downloadUrl = downloadUrl;
    }
}
