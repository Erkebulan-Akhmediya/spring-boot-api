package com.example.for_fun.user;

import com.example.for_fun.user.dto.MeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("user")
@RestController
public class UserController {

    @GetMapping("me")
    public ResponseEntity<MeResponse> me(@AuthenticationPrincipal UserEntity user) {
        try {
            final MeResponse me = MeResponse.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .build();
            return ResponseEntity.ok().body(me);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
