package com.ecommerece.store.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto {
    private int imageId;
    private String imageName;
    private String downloadUrl;
}
