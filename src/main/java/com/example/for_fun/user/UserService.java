package com.example.for_fun.user;

import com.example.for_fun.auth.dto.RegistrationRequest;
import com.example.for_fun.role.RoleService;
import com.example.for_fun.role.exception.RoleNotFoundException;
import com.example.for_fun.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public void create(RegistrationRequest registrationRequest) throws RoleNotFoundException {
        final UserEntity user = UserEntity.builder()
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .username(registrationRequest.getUsername())
                .email(registrationRequest.getEmail())
                .password(this.passwordEncoder.encode(registrationRequest.getPassword()))
                .roles(Set.of(this.roleService.getDefaultRole()))
                .build();

        this.userRepository.save(user);
    }

    public UserEntity getUserByUsername(String username) throws UserNotFoundException {
        try {
            return this.userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        } catch (Exception e) {
            throw new UserNotFoundException();
        }
    }

    public UserDetailsService getUserDetailsService() {
        return this::getUserByUsername;
    }

}
