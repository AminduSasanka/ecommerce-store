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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> convertedProducts = productService.getConvertedProducts(products);

        return ResponseEntity.ok().body(new ApiResponse("Products found", convertedProducts));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getProducts(
            @RequestParam(required = false, name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(required = false, name = "sortingOrder", defaultValue = "asc") String sortOrder,
            @RequestParam(required = false, name = "page", defaultValue = "0") int page,
            @RequestParam(required = false, name = "pageSize", defaultValue = "10") int pageSize
    ) {
        if (pageSize <= 0) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse("Invalid page size", null));
        }
        else if (!sortOrder.equalsIgnoreCase("asc") && !sortOrder.equalsIgnoreCase("desc")) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse("Invalid sort order", null));
        }

        List<Product> products = productService.getAllProducts(pageSize, page, sortBy, sortOrder);
        List<ProductDto> convertedProducts = productService.getConvertedProducts(products);

        return ResponseEntity.ok().body(new ApiResponse("Products found", convertedProducts));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            ProductDto convertedProduct = productService.convertToDto(product);

            return ResponseEntity.ok().body(new ApiResponse("Product found", convertedProduct));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product does not exists", null));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long id, @RequestBody UpdateProductRequest product) {
        try {
            Product updatedProduct = productService.updateProduct(product, id);
            ProductDto convertedProduct = productService.convertToDto(updatedProduct);

            return ResponseEntity.ok().body(new ApiResponse("Product updated", convertedProduct));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found", null));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
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
