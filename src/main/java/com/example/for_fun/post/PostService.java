package com.example.for_fun.post;

import com.example.for_fun.post.dto.PostRequest;
import com.example.for_fun.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    public Long create(PostRequest postRequest, UserEntity author) {
        PostEntity post = PostEntity.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .createdBy(author)
                .build();
        return this.postRepository.save(post).getId();
    }

    public PostEntity get(Long id, UserEntity requestingUser) throws NoSuchElementException, AccessDeniedException {
        PostEntity post = this.postRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Post not found")
        );
        if (post.getCreatedBy().isActive()) return post;
        if (requestingUser.isAdmin()) return post;
        throw new AccessDeniedException("Access denied");
    }

    public List<PostEntity> getAll(Integer page, Integer itemsPerPage) {
        if (page == null || itemsPerPage == null) return this.postRepository.findAll();
        return this.postRepository.findAll(
                PageRequest.of(
                        page,
                        itemsPerPage,
                        Sort.by("createdAt").descending()
                )
        ).getContent();
    }

    public void update(Long id, PostRequest postRequest, UserEntity requestingUser)
            throws NoSuchElementException, AccessDeniedException {

        PostEntity post = this.postRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Post not found")
        );

        if (!Objects.equals(post.getCreatedBy().getId(), requestingUser.getId())) {
            throw new AccessDeniedException("Only the author of the post can update it");
        }

        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        this.postRepository.save(post);

    }

}
