package com.example.simple_insta_backend.repository;

import com.example.simple_insta_backend.entity.Comment;
import com.example.simple_insta_backend.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);
}
