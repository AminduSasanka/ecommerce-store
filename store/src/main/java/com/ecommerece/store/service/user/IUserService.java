package com.ecommerece.store.service.user;

import com.ecommerece.store.dto.UserDto;
import com.ecommerece.store.exception.AlreadyExistException;
import com.ecommerece.store.exception.ResourceNotFoundException;
import com.ecommerece.store.model.User;
import com.ecommerece.store.request.CreateUserRequest;
import com.ecommerece.store.request.UpdateUserRequest;

import java.util.List;

public interface IUserService {
    UserDto getUserById(Long userId) throws ResourceNotFoundException;

    UserDto getUserByFirstname(String firstname);

    UserDto getUserByLastname(String lastName);

    UserDto getUserByEmail(String email);

    UserDto createUser(CreateUserRequest request) throws AlreadyExistException;

    UserDto updateUser(UpdateUserRequest request, Long userId);

    void deleteUser(Long userId) throws ResourceNotFoundException;

    List<UserDto> getAllUsers();

    UserDto convertToDto(User user);
}
