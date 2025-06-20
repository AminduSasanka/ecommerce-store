package com.ecommerece.store.data;

import com.ecommerece.store.model.User;
import com.ecommerece.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        loadDefaultUsers();
    }

    private void loadDefaultUsers() {
        for (int i = 0; i <= 5; i++){
            String defaultEmail = "user" + i + "@email.com";

            if (userRepository.existsByEmail(defaultEmail)){
                continue;
            }

            User user = new User();
            user.setEmail(defaultEmail);
            user.setFirstName("User" + i);
            user.setLastName("User" + i);
            user.setAddress(defaultEmail + "address" + i);
            user.setPhone("011123456" + i);

            userRepository.save(user);

            System.out.println("User " + i + " created");
        }
    }
}
