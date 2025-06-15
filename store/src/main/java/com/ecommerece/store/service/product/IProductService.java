package com.ecommerece.store.service.product;

import com.ecommerece.store.dto.ProductDto;
import com.ecommerece.store.model.Product;
import com.ecommerece.store.request.AddProductRequest;
import com.ecommerece.store.request.UpdateProductRequest;

import java.util.List;

public interface IProductService {
    Product getProductById(Long id);
    Product addProduct(AddProductRequest product);
    Product updateProduct(UpdateProductRequest product, Long id);
    void deleteProduct(Long id);
    List<Product> getAllProducts();
    List<Product> getAllProductsByCategory(String category);
    List<Product> getAllProductsByBrand(String brand);
    List<Product> getAllProductsByCategoryAndBrand(String category, String brand);
    List<Product> getAllProductsByName(String name);
    List<Product> getAllProductsByNameAndBrand(String name, String brand);
    Long countProductsByBrandAndName(String brand, String name);
    ProductDto convertToDto(Product product);
    List<ProductDto> getConvertedProducts(List<Product> products);
}
