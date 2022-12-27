package com.example.simple_insta_backend.service.impl;

import com.example.simple_insta_backend.dto.CommentDto;
import com.example.simple_insta_backend.entity.Comment;
import com.example.simple_insta_backend.entity.Post;
import com.example.simple_insta_backend.entity.User;
import com.example.simple_insta_backend.exception.PostNotFoundException;
import com.example.simple_insta_backend.repository.CommentRepository;
import com.example.simple_insta_backend.repository.PostRepository;
import com.example.simple_insta_backend.repository.UserRepository;
import com.example.simple_insta_backend.service.CommentService;
import com.example.simple_insta_backend.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    // Вернуть все комментарии для поста
    @Override
    public List<Comment> getAllCommentsForPost(Long postId) {
        log.debug("");
        log.debug("Получить все комментарии поста");
        log.debug("  postId: " + postId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));
//        log.debug("  post: " + post);

        List<Comment> allByPost = commentRepository.findAllByPost(post);
        log.debug("  Все комментарии поста: " + allByPost);

        return allByPost;
    }

    // Сохранить комментарий
    @Override
    public Comment saveComment(Long postId, CommentDto commentDto, Principal principal) {
        log.debug("");
        log.debug("Сохранить комментарий");
        log.debug("  postId: " + postId);
        log.debug("  commentDto: " + commentDto);
//        log.debug("  principal: " + principal);

        User user = userService.getUserByPrincipal(principal);
//        log.debug("  user: " + user);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));
        log.debug("");
        log.debug("  post: " + post);

        Comment comment = new Comment();
        comment.setMessage(commentDto.getMessage());
        comment.setCreatedDate(LocalDateTime.now());
        comment.setUsername(user.getUsername());
        comment.setPost(post);
        log.debug("  comment: " + comment);

//        log.debug("  Saving comment for Post: {}", postId);

        Comment savedComment = commentRepository.save(comment);
        log.debug("  savedComment: " + savedComment);
        return savedComment;
    }

    // Пользователь должен иметь возможность удалить комментарий
    @Override
    public void deleteComment(Long commentId) {
        log.debug("");
        log.debug("Удалить комментарий");
        log.debug("  commentId: " + commentId);

        Optional<Comment> optionalComment = commentRepository.findById(commentId);
//        log.debug("  optionalComment: " + optionalComment);

        optionalComment.ifPresent(commentRepository::delete);
    }

    // Вспомогательный метод: достать юзера из объекта Principal
//    private User getUserByPrincipal(Principal principal) {
//        log.debug("");
//        log.debug("Method getUserByPrincipal()");
//        log.debug("  principal: " + principal);
//
//        String username = principal.getName();
//        log.debug("  username: " + username);
//
//        return userRepository.findUserByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
//    }
}
