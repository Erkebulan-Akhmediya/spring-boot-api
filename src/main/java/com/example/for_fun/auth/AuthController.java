package com.example.for_fun.auth;

import com.example.for_fun.auth.dto.LoginRequest;
import com.example.for_fun.auth.dto.LoginResponse;
import com.example.for_fun.auth.dto.RegistrationRequest;
import com.example.for_fun.user.UserEntity;
import com.example.for_fun.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Objects;

@RequiredArgsConstructor
@RequestMapping("auth")
@RestController
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        try {

            if (!Objects.equals(registrationRequest.getPassword(), registrationRequest.getConfirmPassword())) {
                return ResponseEntity.badRequest().build();
            }

            this.userService.create(registrationRequest);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {

            final UserEntity user = this.userService.getUserByUsername(loginRequest.getUsername());
            final String token = this.jwtService.generateToken(new HashMap<>(), user);
            return ResponseEntity.ok(new LoginResponse(token));

        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
