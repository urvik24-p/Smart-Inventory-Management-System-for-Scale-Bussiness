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
    @ConditionalOnProperty(name = "app.bootstrap-admin.username")
    CommandLineRunner bootstrapAdmin(UserRepository users, PasswordEncoder encoder,
                                     @Value("${app.bootstrap-admin.username}") String username,
                                     @Value("${app.bootstrap-admin.password}") String password) {
        return args -> {
            if (users.count() > 0) return;
            if (!username.matches("^[A-Za-z0-9._-]{3,50}$") || password.length() < 12) {
                throw new IllegalStateException("Bootstrap administrator credentials do not meet security requirements.");
            }
            AppUser admin = new AppUser();
            admin.setUsername(username);
            admin.setPassword(encoder.encode(password));
            admin.setRole(UserRole.ADMIN);
            users.save(admin);
        };
    }
}
