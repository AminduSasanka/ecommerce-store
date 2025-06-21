package com.ecommerece.store.repository;

import com.ecommerece.store.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByName(String name);

    List<Role> findByName(String name);
}
