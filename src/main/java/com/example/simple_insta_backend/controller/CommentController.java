package com.example.simple_insta_backend.controller;

import com.example.simple_insta_backend.dto.CommentDto;
import com.example.simple_insta_backend.entity.Comment;
import com.example.simple_insta_backend.facade.CommentFacade;
import com.example.simple_insta_backend.payload.response.MessageResponse;
import com.example.simple_insta_backend.service.CommentService;
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
@RequestMapping("/api/v1/comment")
@CrossOrigin
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentFacade commentFacade;

    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @GetMapping("/{postId}/all")
    public ResponseEntity<List<CommentDto>> getAllCommentsToPost(@PathVariable("postId") String postId) {
        List<CommentDto> commentDtoList = commentService.getAllCommentsForPost(Long.parseLong(postId))
                .stream()
                .map(commentFacade::commentToCommentDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(commentDtoList, HttpStatus.OK);
    }

    @PostMapping("/{postId}/create")
    public ResponseEntity<Object> createComment(@RequestBody @Valid CommentDto commentDto, @PathVariable("postId") String postId,
                                                BindingResult bindingResult, Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Comment saved = commentService.saveComment(Long.parseLong(postId), commentDto, principal);
        CommentDto savedDto = commentFacade.commentToCommentDto(saved);
        return new ResponseEntity<>(savedDto, HttpStatus.OK);
    }

    @PostMapping("/{commentId}/delete")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable("commentId") String commentId) {
        commentService.deleteComment(Long.parseLong(commentId));
        return new ResponseEntity<>(new MessageResponse("Post was deleted"), HttpStatus.OK);
    }
}
