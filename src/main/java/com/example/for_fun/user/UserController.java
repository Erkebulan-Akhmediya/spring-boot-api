package com.example.for_fun.user;

import com.example.for_fun.user.dto.UserResponse;
import com.example.for_fun.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("user")
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("me")
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal UserEntity user) {
        try {
            final UserResponse me = UserResponse.fromUser(user);
            return ResponseEntity.ok().body(me);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("me/deactivate")
    public ResponseEntity<Void> deactivate(@AuthenticationPrincipal UserEntity user) {
        try {
            user.setActive(false);
            this.userService.update(user.getId(), user);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("all")
    public ResponseEntity<List<UserResponse>> all(@RequestParam(name = "is_active", required = false) Boolean isActive) {
        try {
            final List<UserResponse> users = this.userService.getAllUsers(isActive)
                    .stream()
                    .map(UserResponse::fromUser)
                    .toList();

            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
