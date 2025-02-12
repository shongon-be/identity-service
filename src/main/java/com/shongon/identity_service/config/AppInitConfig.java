package com.shongon.identity_service.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.shongon.identity_service.entity.User;
import com.shongon.identity_service.exception.AppException;
import com.shongon.identity_service.exception.ErrorCode;
import com.shongon.identity_service.repository.RoleRepository;
import com.shongon.identity_service.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppInitConfig {

    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    String adminString = "admin";

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        log.info("Application started");
        return args -> {
            if (userRepository.findByUsername(adminString).isEmpty()) {
                var adminRole =
                        roleRepository.findById("USER").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

                User admin = User.builder()
                        .username(adminString)
                        .password(passwordEncoder.encode(adminString))
                        .roles(new HashSet<>(Set.of(adminRole)))
                        .build();

                userRepository.save(admin);
                log.warn("Admin has been created with default password: admin, please change password!");
            }
        };
    }
}
