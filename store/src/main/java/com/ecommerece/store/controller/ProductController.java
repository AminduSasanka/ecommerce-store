package com.ecommerece.store.controller;

import com.ecommerece.store.dto.ProductDto;
import com.ecommerece.store.exception.ProductNotFoundException;
import com.ecommerece.store.model.Product;
import com.ecommerece.store.request.AddProductRequest;
import com.ecommerece.store.request.UpdateProductRequest;
import com.ecommerece.store.response.ApiResponse;
import com.ecommerece.store.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final IProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> convertedProducts = productService.getConvertedProducts(products);

        return ResponseEntity.ok().body(new ApiResponse("Products found", convertedProducts));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable int id) {
        try {
            Product product = productService.getProductById(id);
            ProductDto convertedProduct = productService.convertToDto(product);

            return ResponseEntity.ok().body(new ApiResponse("Product found", convertedProduct));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product does not exists", null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createProduct(@RequestBody AddProductRequest product) {
        try {
            Product newProduct = productService.addProduct(product);

            return ResponseEntity.ok().body(new ApiResponse("Product added", newProduct));
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Product creation failed", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable int id, @RequestBody UpdateProductRequest product) {
        try {
            Product updatedProduct = productService.updateProduct(product, id);
            ProductDto convertedProduct = productService.convertToDto(updatedProduct);

            return ResponseEntity.ok().body(new ApiResponse("Product updated", convertedProduct));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable int id) {
        try {
            productService.deleteProduct(id);

            return ResponseEntity.ok().body(new ApiResponse("Product deleted", null));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found", null));
        }
    }

    @GetMapping("/byCategory/{category}")
    public ResponseEntity<ApiResponse> getAllProductsByCategory(@PathVariable String category) {
        return ResponseEntity.ok()
                .body(new ApiResponse("Products found", productService.getAllProductsByCategory(category)));
    }
}
