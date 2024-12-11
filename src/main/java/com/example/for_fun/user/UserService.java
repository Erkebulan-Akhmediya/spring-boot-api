package com.example.for_fun.user;

import com.example.for_fun.auth.dto.RegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void create(RegistrationDto registrationDto) {
        this.userRepository.save(
                new UserEntity(
                        registrationDto.getFirstName(),
                        registrationDto.getLastName(),
                        registrationDto.getUsername(),
                        registrationDto.getEmail(),
                        registrationDto.getPassword()
                )
        );
    }

    public UserEntity getUserByUsername(String username) {
        return this.userRepository.getAllByUsername(username).getFirst();
    }

}
