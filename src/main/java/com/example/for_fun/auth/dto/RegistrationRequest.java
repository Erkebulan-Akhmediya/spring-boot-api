package com.example.for_fun.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @NotBlank(message = "username cannot be empty")
    private String username;

    private String email;

    @NotBlank(message = "password cannot be empty")
    private String password;

    @JsonProperty("confirm_password")
    private String confirmPassword;

}
