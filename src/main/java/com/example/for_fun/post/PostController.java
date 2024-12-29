package com.example.for_fun.post;

import com.example.for_fun.post.dto.PostRequest;
import com.example.for_fun.post.dto.PostResponse;
import com.example.for_fun.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RequestMapping("post")
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody PostRequest post,
            @AuthenticationPrincipal UserEntity user
    ) {
        try {
            this.postService.create(post, user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<PostResponse> update(
            @PathVariable Long id,
            @RequestBody PostRequest post,
            @AuthenticationPrincipal UserEntity user
    ) {
        try {
            this.postService.update(id, post, user);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException | AccessDeniedException e) {
            return ResponseEntity.badRequest().body(new PostResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
