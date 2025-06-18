package com.ecommerece.store.service.user;

import com.ecommerece.store.exception.AlreadyExistException;
import com.ecommerece.store.exception.ResourceNotFoundException;
import com.ecommerece.store.model.User;
import com.ecommerece.store.repository.UserRepository;
import com.ecommerece.store.request.CreateUserRequest;
import com.ecommerece.store.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;

    @Override
    public User getUserByFirstname(String firstname) {
        return userRepository.findByFirstName(firstname);
    }

    @Override
    public User getUserByLastname(String lastName) {
        return userRepository.findByLastName(lastName);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter((req) -> !userRepository.existsByEmail(req.getEmail()))
                .map(req -> {
                    User user = new User();

                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    user.setPhone(request.getPhone());
                    user.setEmail(request.getEmail());
                    user.setAddress(request.getAddress());

                    return userRepository.save(user);
                })
                .orElseThrow(() -> new AlreadyExistException("User exist with the email: " + request.getEmail()));
    }

    @Override
    public User updateUser(UpdateUserRequest request, Long userId) {
        return userRepository.findById(userId).map(existingUser -> {
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            existingUser.setPhone(request.getPhone());
            existingUser.setAddress(request.getAddress());

            return userRepository.save(existingUser);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .ifPresentOrElse(userRepository::delete,() -> {
                    throw new ResourceNotFoundException("User not found");
                });
    }
}
