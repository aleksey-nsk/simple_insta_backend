package com.example.simple_insta_backend.service.impl;

import com.example.simple_insta_backend.dto.PostDto;
import com.example.simple_insta_backend.entity.Image;
import com.example.simple_insta_backend.entity.Like;
import com.example.simple_insta_backend.entity.Post;
import com.example.simple_insta_backend.entity.User;
import com.example.simple_insta_backend.exception.PostNotFoundException;
import com.example.simple_insta_backend.repository.*;
import com.example.simple_insta_backend.service.PostService;
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
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

    // Вернуть все посты из БД
    // Понадобится когда будем заходить на главную страницу приложения
    @Override
    public List<Post> getAllPosts() {
        log.debug("");
        log.debug("Method getAllPosts()");

        List<Post> all = postRepository.findAllByOrderByCreatedDateDesc();
        log.debug("  all: " + all);

        return all;
    }

    // Вернуть все посты для юзера.
    // Когда будем заходить на страницу профайла юзера, то понадобятся
    // только посты этого юзера
    @Override
    public List<Post> getAllPostsForUser(Principal principal) {
        log.debug("");
        log.debug("Method getAllPostsForUser()");
        log.debug("  principal: " + principal);

//        User user = getUserByPrincipal(principal);
        User user = userService.getUserByPrincipal(principal);
        log.debug("  user: " + user);

        List<Post> all = postRepository.findAllByUserOrderByCreatedDateDesc(user);
        log.debug("  all: " + all);

        return all;
    }

    @Override
    public Post getPostById(Long postId, Principal principal) {
        log.debug("");
        log.debug("Method getPostById()");
        log.debug("  postId: " + postId);
        log.debug("  principal: " + principal);

        // Будем проверять, принадлежит ли пост данному пользователю
//        User user = getUserByPrincipal(principal);
        User user = userService.getUserByPrincipal(principal);
        Post post = postRepository.findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found for user: " + user.getUsername()));

        log.debug("  post: " + post);
        return post;
    }

    // Создать новый пост
    @Override
    public Post createPost(PostDto postDto, Principal principal) {
        log.debug("");
        log.debug("Method createPost()");
        log.debug("  postDto: " + postDto);
//        log.debug("  principal: " + principal);

        // Юзер который будет создавать пост
//        User user = getUserByPrincipal(principal);
        User user = userService.getUserByPrincipal(principal);
        log.debug("  user: " + user);

        Post post = new Post();
        post.setTopic(postDto.getTopic());
        post.setCaption(postDto.getCaption());
        post.setLocation(postDto.getLocation());
        post.setCreatedDate(LocalDateTime.now());
        post.setUser(user);
        // post.setLikes(0);
        log.debug("  post: " + post);

        log.debug("  Saving Post for User: {}", user.getUsername());

        Post postSaved = postRepository.save(post);
        log.debug("  postSaved: " + postSaved);

        return postSaved;
    }

    // Лайкнуть пост
    @Override
    public Post likePost(Long postId, String username) {
        log.debug("");
        log.debug("Method likePost()");
        log.debug("  postId: " + postId);
        log.debug("  username: " + username);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));
        log.debug("  post: " + post);

        // Проверим, есть ли от текущего юзера лайк, или ещё нет
        List<Like> allLikesByPost = likeRepository.findAllByPost(post);
        log.debug("  allLikesByPost: " + allLikesByPost);
        Optional<Like> any = allLikesByPost.stream()
                .filter(like -> like.getUsername().equals(username))
                .findAny();
        log.debug("  any: " + any);

        if (any.isPresent()) {
            log.debug("  Лайк уже есть. Отменяем его");
            likeRepository.delete(any.get());
            return post;
        }

        log.debug("  Сохраним новый лайк в БД");
        Like like = new Like();
        like.setUsername(username);
        like.setPost(post);
        Like savedLike = likeRepository.save(like);
        log.debug("  savedLike: " + savedLike);
        return post;
    }

    // Удалить пост
    @Override
    public void deletePost(Long postId, Principal principal) {
        log.debug("");
        log.debug("Method deletePost()");
        log.debug("  postId: " + postId);
//        log.debug("  principal: " + principal);

        // Будем проверять, принадлежит ли пост данному пользователю
//        User user = getUserByPrincipal(principal);
        User user = userService.getUserByPrincipal(principal);
        Post post = postRepository.findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found for user: " + user.getUsername()));

        post.getComments()
                .forEach(comment -> {
                    log.debug("  Удалим комментарий с commentId=" + comment.getId());
                    commentRepository.deleteById(comment.getId());
                });

        log.debug("  Удалим сам post");
        postRepository.delete(post);

        // Проверим есть ли фотография у этого поста
        Optional<Image> optionalImage = imageRepository.findByPostId(postId);
        log.debug("  optionalImage: " + optionalImage);

        if (optionalImage.isPresent()) {
            log.debug("  У поста есть изображение. Удалим его");
            imageRepository.delete(optionalImage.get());
        }
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
