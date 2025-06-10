package com.ecommerece.store.service.image;

import com.ecommerece.store.dto.ImageDto;
import com.ecommerece.store.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(int id);
    void deleteImageById(int id);
    List<ImageDto> saveImages(List<MultipartFile> files, int productId);
    ImageDto updateImage(MultipartFile file, int imageId);
}
