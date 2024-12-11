package com.example.for_fun.auth;

import com.example.for_fun.auth.dto.LoginDto;
import com.example.for_fun.auth.dto.RegistrationDto;
import com.example.for_fun.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;
import java.util.Objects;

@RequestMapping("auth")
@RestController
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegistrationDto registrationDto) {
        try {

            if (!Objects.equals(registrationDto.getPassword(), registrationDto.getConfirmPassword())) {
                return ResponseEntity.badRequest().build();
            }

            this.userService.create(registrationDto);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginDto loginDto) {
        try {
            this.userService.getUserByUsername(loginDto.getUsername());
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
