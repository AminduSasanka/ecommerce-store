package com.ecommerece.store.service.user;

import com.ecommerece.store.model.User;
import com.ecommerece.store.request.CreateUserRequest;
import com.ecommerece.store.request.UpdateUserRequest;

public interface IUserService {
    User getUserByFirstname(String firstname);

    User getUserByLastname(String lastName);

    User getUserByEmail(String email);

    User createUser(CreateUserRequest request);

    User updateUser(UpdateUserRequest request, Long userId);

    void deleteUser(Long userId);
}
