package com.ecommerece.store.controller;

import com.ecommerece.store.dto.ImageDto;
import com.ecommerece.store.exception.ResourceNotFoundException;
import com.ecommerece.store.model.Image;
import com.ecommerece.store.response.ApiResponse;
import com.ecommerece.store.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {
    private final ImageService imageService;

//    public ResponseEntity<ApiResponse> getImage() {}

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam int productId) {
        try {
            List<ImageDto> images = imageService.saveImages(files, productId);

            return ResponseEntity.ok(new ApiResponse("Upload successful", images));
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Upload failed", e.getMessage()));
        }
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadImage(@PathVariable int id) throws SQLException {
        Image image = imageService.getImageById(id);
        ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename-\"" + image.getFileName() + "\"")
                .body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable int id, @RequestBody MultipartFile file) {
        try {
            Image image = imageService.getImageById(id);

            if(image != null){
                ImageDto updatedImage = imageService.updateImage(file, id);

                return ResponseEntity.ok(new ApiResponse("Update successful", updatedImage));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Image not found!", e.getMessage()));
        }

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Update failed!", null));
    }


    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable int id) {
        try {
            imageService.deleteImageById(id);

            return ResponseEntity.ok().body(new ApiResponse("Image successfully deleted!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("Image not found!", null));
        }
    }
}
