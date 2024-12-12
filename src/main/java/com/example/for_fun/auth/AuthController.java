package com.example.for_fun.auth;

import com.example.for_fun.auth.dto.LoginRequest;
import com.example.for_fun.auth.dto.LoginResponse;
import com.example.for_fun.auth.dto.RegistrationRequest;
import com.example.for_fun.auth.dto.RegistrationResponse;
import com.example.for_fun.role.exception.RoleNotFoundException;
import com.example.for_fun.user.UserEntity;
import com.example.for_fun.user.UserService;
import com.example.for_fun.user.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Objects;

@RequiredArgsConstructor
@RequestMapping("auth")
@RestController
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    @PostMapping("register")
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        try {

            if (!Objects.equals(registrationRequest.getPassword(), registrationRequest.getConfirmPassword())) {
                return ResponseEntity.badRequest().body(new RegistrationResponse("Passwords do not match"));
            }

            this.userService.create(registrationRequest);
            return ResponseEntity.ok().body(new RegistrationResponse(null));

        } catch (RoleNotFoundException e) {
            return ResponseEntity.internalServerError().body(new RegistrationResponse("Role not found"));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(new RegistrationResponse("User already exists"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Transactional
    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {

            this.authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            final UserEntity user = this.userService.getUserByUsername(loginRequest.getUsername());
            final String token = this.jwtService.generateToken(new HashMap<>(), user);
            return ResponseEntity.ok(new LoginResponse(token, null));

        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(new LoginResponse(null, "User not found"));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body(new LoginResponse(null, "Authentication failed"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
