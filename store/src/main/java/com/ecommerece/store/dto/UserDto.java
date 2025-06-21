package com.ecommerece.store.dto;

import com.ecommerece.store.model.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private Set<RoleDto> roles;
}
