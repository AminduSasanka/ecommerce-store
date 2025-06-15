package com.ecommerece.store.repository;

import com.ecommerece.store.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    List<Image> findByProductId(int id);
}
