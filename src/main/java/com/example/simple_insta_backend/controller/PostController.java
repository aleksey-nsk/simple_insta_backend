package com.example.simple_insta_backend.controller;

import com.example.simple_insta_backend.dto.PostDto;
import com.example.simple_insta_backend.entity.Post;
import com.example.simple_insta_backend.facade.PostFacade;
import com.example.simple_insta_backend.payload.response.MessageResponse;
import com.example.simple_insta_backend.service.PostService;
import com.example.simple_insta_backend.validator.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/post")
@CrossOrigin
public class PostController {

    @Autowired
    private PostFacade postFacade;

    @Autowired
    private PostService postService;

    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @GetMapping("/all")
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<PostDto> postDtoList = postService.getAllPosts()
                .stream()
                .map(post -> postFacade.postToPostDto(post))
                .collect(Collectors.toList());

        return new ResponseEntity<>(postDtoList, HttpStatus.OK);
    }

    @GetMapping("/user/posts")
    public ResponseEntity<List<PostDto>> getAllPostsForUser(Principal principal) {
        List<PostDto> postDtoList = postService.getAllPostsForUser(principal)
                .stream()
                .map(postFacade::postToPostDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(postDtoList, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createPost(@RequestBody @Valid PostDto postDto,
                                             BindingResult bindingResult,
                                             Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Post created = postService.createPost(postDto, principal);
        PostDto createdDto = postFacade.postToPostDto(created);
        return new ResponseEntity<>(createdDto, HttpStatus.OK);
    }

    @PostMapping("/{postId}/{username}/like")
    public ResponseEntity<PostDto> likePost(@PathVariable("postId") String postId,
                                            @PathVariable("username") String username) {
        Post likedPost = postService.likePost(Long.parseLong(postId), username);
        PostDto likedPostDto = postFacade.postToPostDto(likedPost);
        return new ResponseEntity<>(likedPostDto, HttpStatus.OK);
    }

    @PostMapping("/{postId}/delete")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable("postId") String postId, Principal principal) {
        postService.deletePost(Long.parseLong(postId), principal);
        return new ResponseEntity<>(new MessageResponse("Post was deleted"), HttpStatus.OK);
    }
}
