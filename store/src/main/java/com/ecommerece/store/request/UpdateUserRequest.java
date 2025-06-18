package com.ecommerece.store.request;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
}
