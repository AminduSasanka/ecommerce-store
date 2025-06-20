package com.ecommerece.store.controller;

import com.ecommerece.store.exception.AlreadyExistException;
import com.ecommerece.store.exception.ResourceNotFoundException;
import com.ecommerece.store.model.User;
import com.ecommerece.store.request.CreateUserRequest;
import com.ecommerece.store.request.UpdateUserRequest;
import com.ecommerece.store.response.ApiResponse;
import com.ecommerece.store.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse> getUsers() {
        try {
            return ResponseEntity.ok().body(new ApiResponse("Users found", userService.getAllUsers()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Internal server error", e.getMessage()));
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(new ApiResponse("User found", userService.getUserById(id)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("User found", null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Internal server error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
        try {
            User user = userService.createUser(request);

            return ResponseEntity.ok().body(new ApiResponse("User created", user));
        } catch (AlreadyExistException e) {
            return ResponseEntity.status(CONFLICT)
                    .body(new ApiResponse("User already exist",null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Internal server error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        try {
            User user = userService.updateUser(request, id);

            return ResponseEntity.ok()
                    .body(new ApiResponse("User updated", user));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("User not found", null));
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);

            return ResponseEntity.ok().body(new ApiResponse("User deleted", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("User not found", e.getMessage()));
        }
    }
}
