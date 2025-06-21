package com.ecommerece.store.data;

import com.ecommerece.store.model.Role;
import com.ecommerece.store.model.User;
import com.ecommerece.store.repository.RoleRepository;
import com.ecommerece.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        loadDefaultUsers();
        createDefaultRoles();
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
            user.setPassword(passwordEncoder.encode("P@ssw0rd"));

            userRepository.save(user);

            System.out.println("User " + i + " created");
        }
    }

    private void createDefaultRoles() {
        List<String> roles = Arrays.asList("USER", "ADMIN");

        roles.stream()
                .filter((role) -> !roleRepository.existsByName(role))
                .forEach((role) -> {
                    Role roleEntity = new Role();

                    roleEntity.setName(role);
                    roleRepository.save(roleEntity);
                });
    }
}
