package com.example.simple_insta_backend.repository;

import com.example.simple_insta_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserById(Long userId);

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);
}
