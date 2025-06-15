package com.ecommerece.store.service.product;

import com.ecommerece.store.dto.ImageDto;
import com.ecommerece.store.dto.ProductDto;
import com.ecommerece.store.exception.ProductNotFoundException;
import com.ecommerece.store.model.Category;
import com.ecommerece.store.model.Image;
import com.ecommerece.store.model.Product;
import com.ecommerece.store.repository.CategoryRepository;
import com.ecommerece.store.repository.ImageRepository;
import com.ecommerece.store.repository.ProductRepository;
import com.ecommerece.store.request.AddProductRequest;
import com.ecommerece.store.request.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    @Override
    public Product getProductById(Long id) {
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
    public Product updateProduct(UpdateProductRequest product, Long id) {
        Product existingProduct = productRepository
                .findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found!"));

        Product updatedProduct = updateProduct(existingProduct, product);

        return productRepository.save(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
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

    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream().map(image -> modelMapper.map(image, ImageDto.class)).toList();

        productDto.setImages(imageDtos);

        return productDto;
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products){
        return products.stream().map(this::convertToDto).toList();
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
