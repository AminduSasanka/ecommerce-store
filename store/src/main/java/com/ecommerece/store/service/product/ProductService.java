package com.ecommerece.store.service.product;

import com.ecommerece.store.exception.ProductNotFoundException;
import com.ecommerece.store.model.Category;
import com.ecommerece.store.model.Product;
import com.ecommerece.store.repository.CategoryRepository;
import com.ecommerece.store.repository.ProductRepository;
import com.ecommerece.store.request.AddProductRequest;
import com.ecommerece.store.request.UpdateProductRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product getProductById(int id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found!"));
    }

    @Override
    public Product addProduct(AddProductRequest product) {
        Category foundCategory = Optional.ofNullable(categoryRepository.findByName(product.getCategory()))
                .orElseGet(() -> {
                    Category newCategory = new Category(product.getCategory());
                    return categoryRepository.save(newCategory);
                });

        Product newProduct = createProduct(product, foundCategory);

        return productRepository.save(newProduct);
    }

    @Override
    public Product updateProduct(UpdateProductRequest product, int id) {
        Product existingProduct = productRepository
                .findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found!"));

        Product updatedProduct = updateProduct(existingProduct, product);

        return productRepository.save(updatedProduct);
    }

    @Override
    public void deleteProduct(int id) {
         productRepository
                .findById(id)
                .ifPresentOrElse(
                        productRepository::delete,
                        ()-> {throw new ProductNotFoundException("Product not found!");}
                );
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getAllProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getAllProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getAllProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getAllProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getAllProductsByNameAndBrand(String name, String brand) {
        return productRepository.findByNameAndBrand(name, brand);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countProductByBrandAndName(brand, name);
    }

    private Product createProduct(AddProductRequest product, Category category){
        return new Product(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getBrand(),
                category
        );
    }

    private Product updateProduct(Product product, UpdateProductRequest updateProductRequest) {
        Category category = categoryRepository.findByName(updateProductRequest.getCategory().getName());

        product.setName(updateProductRequest.getName());
        product.setDescription(updateProductRequest.getDescription());
        product.setPrice(updateProductRequest.getPrice());
        product.setQuantity(updateProductRequest.getQuantity());
        product.setBrand(updateProductRequest.getBrand());
        product.setCategory(category);

        return product;
    }
}
