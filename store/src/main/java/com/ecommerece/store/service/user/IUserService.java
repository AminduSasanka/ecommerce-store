package com.ecommerece.store.service.user;

import com.ecommerece.store.model.User;

public interface IUserService {
    User getUserByFirstname(String firstname);

    User getUserByLastname(String lastName);

    User getUserByEmail(String email);
}
