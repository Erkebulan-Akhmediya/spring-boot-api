package com.example.for_fun.role;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Order(1)
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {

        if (this.roleRepository.getByName("user").isEmpty()) {
            this.roleRepository.save(new RoleEntity("user"));
        }

        if (this.roleRepository.getByName("admin").isEmpty()) {
            this.roleRepository.save(new RoleEntity("admin"));
        }

    }

}
