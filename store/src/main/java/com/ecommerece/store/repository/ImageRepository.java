package com.ecommerece.store.repository;

import com.ecommerece.store.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Integer> {
}
