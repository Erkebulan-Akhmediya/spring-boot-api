package com.example.for_fun.user;

import com.example.for_fun.auth.dto.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void create(RegistrationRequest registrationRequest) {
        this.userRepository.save(
                new UserEntity(
                        registrationRequest.getFirstName(),
                        registrationRequest.getLastName(),
                        registrationRequest.getUsername(),
                        registrationRequest.getEmail(),
                        this.passwordEncoder.encode(registrationRequest.getPassword())
                )
        );
    }

    public UserEntity getUserByUsername(String username) {
        return this.userRepository.getAllByUsername(username).getFirst();
    }

    public UserDetailsService getUserDetailsService() {
        return this::getUserByUsername;
    }

}
