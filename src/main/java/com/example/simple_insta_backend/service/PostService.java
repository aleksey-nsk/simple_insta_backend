package com.example.simple_insta_backend.service;

import com.example.simple_insta_backend.dto.PostDto;
import com.example.simple_insta_backend.entity.Post;

import java.security.Principal;
import java.util.List;

public interface PostService {

    List<Post> getAllPosts();

    List<Post> getAllPostsForUser(Principal principal);

    Post getPostById(Long postId, Principal principal);

    Post createPost(PostDto postDto, Principal principal);

    Post likePost(Long postId, String username);

    void deletePost(Long postId, Principal principal);
}
