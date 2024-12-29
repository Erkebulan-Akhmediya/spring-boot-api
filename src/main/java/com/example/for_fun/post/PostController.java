package com.example.for_fun.post;

import com.example.for_fun.post.dto.CreatePostResponse;
import com.example.for_fun.post.dto.GetPostResponse;
import com.example.for_fun.post.dto.PostRequest;
import com.example.for_fun.post.dto.UpdatePostResponse;
import com.example.for_fun.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RequestMapping("post")
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<CreatePostResponse> create(
            @RequestBody PostRequest post,
            @AuthenticationPrincipal UserEntity author
    ) {
        try {
            Long id = this.postService.create(post, author);
            return ResponseEntity.ok(new CreatePostResponse(id));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<GetPostResponse> get(
            @PathVariable Long id,
            @AuthenticationPrincipal UserEntity requestingUser
    ) {
        try {
            PostEntity post = this.postService.get(id, requestingUser);
            return ResponseEntity.ok(GetPostResponse.fromPost(post));
        } catch (NoSuchElementException | AccessDeniedException e) {
            return ResponseEntity.badRequest().body(new GetPostResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<GetPostResponse>> getAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(name = "items_per_page", required = false) Integer itemsPerPage
    ) {
        try {
            if (page == null && itemsPerPage != null) page = 0;
            if (page != null && itemsPerPage == null) itemsPerPage = 10;
            List<GetPostResponse> posts = this.postService.getAll(page, itemsPerPage)
                    .stream()
                    .map(GetPostResponse::fromPost)
                    .toList();
            return ResponseEntity.ok().body(posts);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<UpdatePostResponse> update(
            @PathVariable Long id,
            @RequestBody PostRequest post,
            @AuthenticationPrincipal UserEntity requestingUser
    ) {
        try {
            this.postService.update(id, post, requestingUser);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException | AccessDeniedException e) {
            return ResponseEntity.badRequest().body(new UpdatePostResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
