package com.example.simple_insta_backend.service.impl;

import com.example.simple_insta_backend.dto.PostDto;
import com.example.simple_insta_backend.entity.Image;
import com.example.simple_insta_backend.entity.Like;
import com.example.simple_insta_backend.entity.Post;
import com.example.simple_insta_backend.entity.User;
import com.example.simple_insta_backend.exception.PostNotFoundException;
import com.example.simple_insta_backend.repository.CommentRepository;
import com.example.simple_insta_backend.repository.ImageRepository;
import com.example.simple_insta_backend.repository.LikeRepository;
import com.example.simple_insta_backend.repository.PostRepository;
import com.example.simple_insta_backend.service.PostService;
import com.example.simple_insta_backend.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ImageRepository imageRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

    // Получить все посты из БД.
    // Понадобится когда будем заходить на главную страницу приложения
    @Override
    public List<Post> getAllPosts() {
        log.debug("");
        log.debug("Получить все посты");

        List<Post> all = postRepository.findAllByOrderByCreatedDateDesc();
        log.debug("  all: " + all);

        return all;
    }

    // Получить все посты текущего юзера.
    // Когда будем заходить в профиль юзера, то понадобятся посты только этого юзера
    @Override
    public List<Post> getAllPostsForUser(Principal principal) {
        log.debug("");
        log.debug("Получить все посты текущего юзера");

        User user = userService.getUserByPrincipal(principal);
        List<Post> all = postRepository.findAllByUserOrderByCreatedDateDesc(user);

        log.debug("");
        log.debug("  all: " + all);
        return all;
    }

    // Создать новый пост
    @Override
    public Post createPost(PostDto postDto, Principal principal) {
        log.debug("");
        log.debug("Создать новый пост");
        log.debug("  postDto: " + postDto);

        // Юзер который будет создавать пост
        User user = userService.getUserByPrincipal(principal);

        Post post = new Post();
        post.setTopic(postDto.getTopic());
        post.setCaption(postDto.getCaption());
        post.setLocation(postDto.getLocation());
        post.setCreatedDate(LocalDateTime.now());
        post.setUser(user);

        log.debug("");
        log.debug("  post: " + post);

        Post postSaved = postRepository.save(post);
        log.debug("  postSaved: " + postSaved);
        return postSaved;
    }

    // Лайкнуть пост
    @Override
    public Post likePost(Long postId, String username) {
        log.debug("");
        log.debug("Лайкнуть пост");
        log.debug("  postId: " + postId);
        log.debug("  username: " + username);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));
        log.debug("  post: " + post);

        // Проверим, есть ли лайк от данного юзера, или ещё нет
        List<Like> allLikesByPost = likeRepository.findAllByPost(post);
        log.debug("  Все лайки поста: " + allLikesByPost);
        Optional<Like> any = allLikesByPost.stream()
                .filter(like -> like.getUsername().equals(username))
                .findAny();
        log.debug("  Есть ли лайк от данного юзера: " + any);

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
        log.debug("Удалить пост");
        log.debug("  postId: " + postId);

        // Проверим принадлежит ли пост данному пользователю
        User user = userService.getUserByPrincipal(principal);
        Post post = postRepository.findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found for user: " + user.getUsername()));

        log.debug("");

        post.getComments()
                .forEach(comment -> {
                    log.debug("  Удалим комментарий с commentId=" + comment.getId());
                    commentRepository.deleteById(comment.getId());
                });

        post.getLikes()
                .forEach(like -> {
                    log.debug("  Удалим лайк с likeId=" + like.getId());
                    likeRepository.deleteById(like.getId());
                });

        log.debug("  Удалим сам post");
        postRepository.delete(post);

        Optional<Image> optionalImage = imageRepository.findByPostId(postId);
        log.debug("  Есть ли картинка у данного поста: " + optionalImage);

        if (optionalImage.isPresent()) {
            log.debug("  У поста есть картинка. Удалим её");
            imageRepository.delete(optionalImage.get());
        }
    }
}
