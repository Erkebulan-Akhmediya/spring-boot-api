package com.example.for_fun.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    Page<PostEntity> findAllByCreatedAtAfter(Date createdAtAfter, Pageable pageable);

    List<PostEntity> findAllByCreatedAtAfter(Date createdAtAfter);
}
