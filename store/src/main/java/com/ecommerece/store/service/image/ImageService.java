package com.ecommerece.store.service.image;

import com.ecommerece.store.dto.ImageDto;
import com.ecommerece.store.exception.ResourceNotFoundException;
import com.ecommerece.store.model.Image;
import com.ecommerece.store.model.Product;
import com.ecommerece.store.repository.ImageRepository;
import com.ecommerece.store.service.product.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ImageService implements IImageService{
    private final ImageRepository imageRepository;
    private final ProductService productService;

    @Override
    public Image getImageById(int id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found!"));
    }

    @Override
    public void deleteImageById(int id) {
        Optional.ofNullable(getImageById(id))
                .ifPresentOrElse(imageRepository::delete, () -> {
                    throw new ResourceNotFoundException("Image not found!");
                });
    }

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, int productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> imageDtos = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                String downloadUrl = "api/v1/images/";

                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setDownloadUrl(downloadUrl);
                image.setProduct(product);

                Image savedImage = imageRepository.save(image);

                savedImage.setDownloadUrl(downloadUrl + savedImage.getId() + "/download");
                imageRepository.save(savedImage);

                imageDtos.add(new ImageDto(savedImage.getId(), savedImage.getFileName(), savedImage.getDownloadUrl()));

            } catch (IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        return imageDtos;
    }

    @Override
    public ImageDto updateImage(MultipartFile file, int imageId) {
        Image image = getImageById(imageId);

        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));

            Image savedImage = imageRepository.save(image);

            return new ImageDto(savedImage.getId(), savedImage.getFileName(), savedImage.getDownloadUrl());
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
