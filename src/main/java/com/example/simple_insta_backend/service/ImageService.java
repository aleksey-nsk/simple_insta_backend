package com.example.simple_insta_backend.service;

import com.example.simple_insta_backend.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

public interface ImageService {

    Image uploadImageToUser(MultipartFile file, Principal principal) throws IOException;

    Image uploadImageToPost(MultipartFile file, Principal principal, Long postId) throws IOException;

    Image getUserImage(Principal principal);

    Image getImageToPost(Long postId);
}
