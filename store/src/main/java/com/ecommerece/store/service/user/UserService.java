package com.ecommerece.store.service.user;

import com.ecommerece.store.model.User;
import com.ecommerece.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
