package com.ecommerece.store.service.user;

import com.ecommerece.store.dto.RoleDto;
import com.ecommerece.store.dto.UserDto;
import com.ecommerece.store.exception.AlreadyExistException;
import com.ecommerece.store.exception.ResourceNotFoundException;
import com.ecommerece.store.model.User;
import com.ecommerece.store.repository.UserRepository;
import com.ecommerece.store.request.CreateUserRequest;
import com.ecommerece.store.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto getUserById(Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return convertToDto(user);
    }


    @Override
    public UserDto getUserByFirstname(String firstname) {
        User user = userRepository.findByFirstName(firstname);

        return convertToDto(user);
    }

    @Override
    public UserDto getUserByLastname(String lastName) {
        User user = userRepository.findByLastName(lastName);

        return convertToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);

        return convertToDto(user);
    }

    @Override
    public UserDto createUser(CreateUserRequest request) throws AlreadyExistException {
        return Optional.of(request)
                .filter((req) -> !userRepository.existsByEmail(req.getEmail()))
                .map(req -> {
                    User user = new User();

                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    user.setPhone(request.getPhone());
                    user.setEmail(request.getEmail());
                    user.setAddress(request.getAddress());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));

                    userRepository.save(user);

                    return convertToDto(user);
                })
                .orElseThrow(() -> new AlreadyExistException("User exist with the email: " + request.getEmail()));
    }

    @Override
    public UserDto updateUser(UpdateUserRequest request, Long userId) {
        return userRepository.findById(userId).map(existingUser -> {
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            existingUser.setPhone(request.getPhone());
            existingUser.setAddress(request.getAddress());

            userRepository.save(existingUser);

            return convertToDto(existingUser);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public void deleteUser(Long userId) throws ResourceNotFoundException {
        userRepository.findById(userId)
                .ifPresentOrElse(userRepository::delete,() -> {
                    throw new ResourceNotFoundException("User not found");
                });
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::convertToDto).toList();
    }

    @Override
    public UserDto convertToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        Set<RoleDto> roleDtos = user.getRoles().stream()
                .map(role -> modelMapper.map(role, RoleDto.class))
                .collect(Collectors.toSet());

        userDto.setRoles(roleDtos);

        return userDto;
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userRepository.findByEmail(email);
    }
}
