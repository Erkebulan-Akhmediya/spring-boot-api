package com.example.for_fun.post;

import com.example.for_fun.post.dto.PostRequest;
import com.example.for_fun.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    public void create(PostRequest postRequest, UserEntity user) {
        PostEntity post = PostEntity.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .createdBy(user)
                .build();
        this.postRepository.save(post);
    }

    public void update(Long id, PostRequest postRequest, UserEntity user)
            throws NoSuchElementException, AccessDeniedException {

        PostEntity post = this.postRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Post not found")
        );

        if (!Objects.equals(post.getCreatedBy().getId(), user.getId())) {
            throw new AccessDeniedException("The user doesn't have permission to update this post");
        }

        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        this.postRepository.save(post);

    }

}
