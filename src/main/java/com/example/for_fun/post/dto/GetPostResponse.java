package com.example.for_fun.post.dto;

import com.example.for_fun.post.PostEntity;
import com.example.for_fun.user.dto.UserResponse;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GetPostResponse {
    private String error;
    private Long id;
    private String title;
    private String content;
    private UserResponse author;

    public GetPostResponse(String error) {
        this.error = error;
    }

    public static GetPostResponse fromPost(PostEntity post) {
        UserResponse author = UserResponse.fromUser(post.getCreatedBy());
        return GetPostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(author)
                .build();
    }

}
