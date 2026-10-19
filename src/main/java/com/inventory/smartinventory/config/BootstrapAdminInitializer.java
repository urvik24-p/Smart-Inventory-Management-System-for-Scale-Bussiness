package com.inventory.smartinventory.config;

import com.inventory.smartinventory.entity.AppUser;
import com.inventory.smartinventory.entity.UserRole;
import com.inventory.smartinventory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/** Creates the first administrator only when explicitly enabled by deployment secrets. */
@Configuration
public class BootstrapAdminInitializer {
    @Bean
    CommandLineRunner bootstrapAdmin(UserRepository users, PasswordEncoder encoder,
                                     @Value("${app.bootstrap-admin.username:admin}") String username,
                                     @Value("${app.bootstrap-admin.password:Admin@12345678}") String password) {
        return args -> {
            try {
                if (users.count() > 0) return;
                String user = (username != null && !username.isBlank()) ? username.trim() : "admin";
                String pass = (password != null && !password.isBlank()) ? password.trim() : "Admin@12345678";

                AppUser admin = new AppUser();
                admin.setUsername(user);
                admin.setPassword(encoder.encode(pass));
                admin.setRole(UserRole.ADMIN);
                users.save(admin);
                System.out.println(">>> [INIT] Created initial administrator account: " + user);
            } catch (Exception e) {
                System.err.println(">>> [INIT] Warning: Could not create bootstrap admin: " + e.getMessage());
            }
        };
    }
}
