package com.example.simple_insta_backend.facade;

import com.example.simple_insta_backend.dto.PostDto;
import com.example.simple_insta_backend.entity.Like;
import com.example.simple_insta_backend.entity.Post;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component // так как будем потом делать его инжекцию в .... ???
@Log4j2
public class PostFacade {

    public PostDto postToPostDto(Post post) {
        log.debug("");
        log.debug("Method postToPostDto()");
        log.debug("  post: " + post);

        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTopic(post.getTopic());
        postDto.setCaption(post.getCaption());
        postDto.setLocation(post.getLocation());
        postDto.setUsername(post.getUser().getUsername());

        List<Like> likes = post.getLikes();
        if (likes.isEmpty()) {
            postDto.setLikes(0);
        } else {
            postDto.setLikes(likes.size());
        }

        // postDto.setUsersLiked(post.getLikedUsers());
        Set<String> usersLiked = post.getLikes().stream()
                .map(like -> like.getUsername())
                .collect(Collectors.toSet());
//        log.debug("  usersLiked: " + usersLiked);
        postDto.setUsersLiked(usersLiked);

        log.debug("  postDto: " + postDto);
        return postDto;
    }
}
