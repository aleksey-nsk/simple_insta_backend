package com.example.simple_insta_backend.service;

import com.example.simple_insta_backend.dto.CommentDto;
import com.example.simple_insta_backend.entity.Comment;

import java.security.Principal;
import java.util.List;

public interface CommentService {

    List<Comment> getAllCommentsForPost(Long postId);

    Comment saveComment(Long postId, CommentDto commentDto, Principal principal);

    void deleteComment(Long commentId);
}
