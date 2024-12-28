package com.example.for_fun.post;

import com.example.for_fun.post.dto.CreatePostRequest;
import com.example.for_fun.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    public void create(UserEntity user, CreatePostRequest postRequest) {
        PostEntity post = PostEntity.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .createdBy(user)
                .build();
        this.postRepository.save(post);
    }

}
