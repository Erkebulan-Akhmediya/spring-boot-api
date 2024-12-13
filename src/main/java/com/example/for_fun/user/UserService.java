package com.example.for_fun.user;

import com.example.for_fun.auth.dto.RegistrationRequest;
import com.example.for_fun.role.RoleService;
import com.example.for_fun.role.exception.RoleNotFoundException;
import com.example.for_fun.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<UserEntity> getAllUsers(Boolean isActive) {
        if (isActive == null) return this.userRepository.findAll();
        return this.userRepository.findAllByIsActive(isActive);
    }

    public void update(Long id, UserEntity newUser) throws UserNotFoundException {
        final UserEntity oldUser = this.userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        oldUser.setFirstName(newUser.getFirstName());
        oldUser.setLastName(newUser.getLastName());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setUsername(newUser.getUsername());
        oldUser.setActive(newUser.isActive());

        if (newUser.getPassword() != null && !newUser.getPassword().isEmpty()) {
            oldUser.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
        }

        if (newUser.getRoles() != null && !newUser.getRoles().isEmpty()) {
            oldUser.setRoles(newUser.getRoles());
        }

        this.userRepository.save(oldUser);
    }

    public UserDetailsService getUserDetailsService() {
        return this::getUserByUsername;
    }

}
