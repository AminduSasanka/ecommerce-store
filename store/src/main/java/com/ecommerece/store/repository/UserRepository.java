package com.ecommerece.store.repository;

import com.ecommerece.store.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByFirstName(String firstname);

    User findByLastName(String lastName);

    User findByEmail(String email);
}
