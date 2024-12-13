package com.example.for_fun.user;

import com.example.for_fun.role.RoleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@RequiredArgsConstructor
@Component
@Order(2)
public class UserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        final boolean adminExists = !this.userRepository.findByRolesContaining(
                new HashSet<>(){{ add(new RoleEntity("admin")); }}
        ).isEmpty();
        if (adminExists) return;

        final UserEntity user = UserEntity.builder()
                .firstName("admin")
                .lastName("admin")
                .email("admin@email.com")
                .username("admin")
                .password(this.passwordEncoder.encode("admin"))
                .roles(new HashSet<>(){{ add(new RoleEntity("admin")); }})
                .build();
        this.userRepository.save(user);

    }

}
