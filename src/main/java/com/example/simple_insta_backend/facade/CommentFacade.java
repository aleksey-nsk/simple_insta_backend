package com.example.simple_insta_backend.facade;

import com.example.simple_insta_backend.dto.CommentDto;
import com.example.simple_insta_backend.entity.Comment;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class CommentFacade {

    public CommentDto commentToCommentDto(Comment comment) {
        log.debug("");
        log.debug("Method commentToCommentDto()");
        log.debug("  comment: " + comment);

        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setMessage(comment.getMessage());
        commentDto.setUsername(comment.getUsername());

        log.debug("  commentDto: " + commentDto);
        return commentDto;
    }
}
