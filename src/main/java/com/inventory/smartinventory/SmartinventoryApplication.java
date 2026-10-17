package com.inventory.smartinventory;

import com.inventory.smartinventory.entity.AppUser;
import com.inventory.smartinventory.entity.UserRole;
import com.inventory.smartinventory.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SmartinventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartinventoryApplication.class, args);
	}

	/**
	 * Automatically seed the default admin user on startup if the users table is empty.
	 * Password is set to "admin123".
	 */
	@Bean
	CommandLineRunner initAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (!userRepository.existsByUsername("admin")) {
				AppUser admin = new AppUser();
				admin.setUsername("admin");
				admin.setPassword(passwordEncoder.encode("admin123"));
				admin.setRole(UserRole.ADMIN);
				userRepository.save(admin);
				System.out.println(">>> [INIT] Default 'admin' user created successfully.");
			}
		};
	}

}
