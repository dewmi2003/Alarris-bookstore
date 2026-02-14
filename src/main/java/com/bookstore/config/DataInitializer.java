package com.bookstore.config;

import com.bookstore.entity.Role;
import com.bookstore.entity.User;
import com.bookstore.repository.RoleRepository;
import com.bookstore.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Configuration
@Slf4j
public class DataInitializer {

    @Bean
    @Transactional
    public CommandLineRunner initData(RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            log.info("Initializing administrative data...");

            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            if (adminRole == null) {
                log.info("Creating ROLE_ADMIN");
                adminRole = Role.builder().name("ROLE_ADMIN").build();
                adminRole = roleRepository.save(adminRole);
            }

            Role customerRole = roleRepository.findByName("ROLE_CUSTOMER");
            if (customerRole == null) {
                log.info("Creating ROLE_CUSTOMER");
                customerRole = Role.builder().name("ROLE_CUSTOMER").build();
                customerRole = roleRepository.save(customerRole);
            }

            if (userRepository.findByEmail("admin@bookstore.com").isEmpty()) {
                log.info("Creating default admin user");
                User admin = User.builder()
                        .fullName("Admin User")
                        .email("admin@bookstore.com")
                        .password(passwordEncoder.encode("admin123"))
                        .address("System Admin Address")
                        .roles(Collections.singletonList(adminRole))
                        .build();
                userRepository.save(admin);
            }
            log.info("Data initialization complete.");
        };
    }
}
