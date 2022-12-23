package com.example.simple_insta_backend.repository;

import com.example.simple_insta_backend.entity.Like;
import com.example.simple_insta_backend.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    List<Like> findAllByPost(Post post);
}
