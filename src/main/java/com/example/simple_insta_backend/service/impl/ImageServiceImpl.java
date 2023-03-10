package com.example.simple_insta_backend.service.impl;

import com.example.simple_insta_backend.entity.Image;
import com.example.simple_insta_backend.entity.Post;
import com.example.simple_insta_backend.entity.User;
import com.example.simple_insta_backend.exception.ImageNotFoundException;
import com.example.simple_insta_backend.repository.ImageRepository;
import com.example.simple_insta_backend.service.ImageService;
import com.example.simple_insta_backend.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

// Сервис, который будет создавать картинку
// для поста, либо для юзера.
//
// И возвращать картинку.

@Service
@Log4j2
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserService userService;

    @Override
    public Image uploadImageToUser(MultipartFile file, Principal principal) throws IOException {
        log.debug("");
        log.debug("Загрузить фото в профиль юзера");
        log.debug("  Выбран ли файл с картинкой для загрузки: " + !file.isEmpty());

        User user = userService.getUserByPrincipal(principal);

        Image userProfileImage = imageRepository.findByUserId(user.getId()).orElse(null);
        log.debug("");
        log.debug("  userProfileImage: " + userProfileImage);

        if (!ObjectUtils.isEmpty(userProfileImage)) {
            log.debug("  Фото у юзера уже есть => сначала удалим старое, а потом загрузим новое");
            imageRepository.delete(userProfileImage);
        }

        Image image = new Image();
        image.setTitle(file.getOriginalFilename());
        image.setImageBytes(compressBytes(file.getBytes())); // сжимаем картинку
        image.setUserId(user.getId());
        log.debug("");
        log.debug("  image: " + image);

        Image savedImg = imageRepository.save(image);
        log.debug("  savedImg: " + savedImg);
        return savedImg;
    }

    @Override
    public Image uploadImageToPost(MultipartFile file, Principal principal, Long postId) throws IOException {
        log.debug("");
        log.debug("Загрузить картинку посту");
        log.debug("  postId: " + postId);
        log.debug("  Выбран ли файл с картинкой для загрузки: " + !file.isEmpty());

        User user = userService.getUserByPrincipal(principal);

        Post post = user.getPosts()
                .stream()
                .filter(p -> p.getId().equals(postId))
                .collect(toSinglePostCollector());
        log.debug("");
        log.debug("  post: " + post);

        Image image = new Image();
        image.setTitle(file.getOriginalFilename());
        image.setImageBytes(compressBytes(file.getBytes())); // сжимаем картинку
        image.setPostId(post.getId());
        log.debug("");
        log.debug("  image: " + image);

        Image savedImg = imageRepository.save(image);
        log.debug("  savedImg: " + savedImg);
        return savedImg;
    }

    @Override
    public Image getUserImage(Principal principal) {
        log.debug("");
        log.debug("Получить фото юзера");

        User user = userService.getUserByPrincipal(principal);

        Image image = imageRepository.findByUserId(user.getId()).orElse(null);
        log.debug("");
        log.debug("  image: " + image);

        if (!ObjectUtils.isEmpty(image)) {
            byte[] initial = image.getImageBytes();
            byte[] result = decompressBytes(initial);
            image.setImageBytes(result);
        }

        log.debug("");
        log.debug("  Вернуть изображение: " + image);
        return image;
    }

    @Override
    public Image getImageToPost(Long postId) {
        log.debug("");
        log.debug("Получить картинку поста");
        log.debug("  postId: " + postId);

        Image image = imageRepository
                .findByPostId(postId)
                .orElseThrow(() -> new ImageNotFoundException("Cannot find image for Post: " + postId));
        log.debug("  image: " + image);

        if (!ObjectUtils.isEmpty(image)) {
            byte[] initial = image.getImageBytes();
            byte[] result = decompressBytes(initial);
            image.setImageBytes(result);
        }

        log.debug("");
        log.debug("  Вернуть картинку: " + image);
        return image;
    }

    // Метод, который вернёт один единственный пост
    // для пользователя (у юзера может быть много постов)
    private <T> Collector<T, ?, T> toSinglePostCollector() {
        return Collectors.collectingAndThen(Collectors.toList(), list -> {
            if (list.size() != 1) {
                throw new IllegalStateException();
            }
            return list.get(0);
        });
    }

    // Вспомогательный методы для компрессии картинок.
    // Перед тем как сохранить файл картинки в БД, мы будем делать компрессию,
    // то есть уменьшать количество байтов в файле
    private byte[] compressBytes(byte[] data) {
        log.debug("");
        log.debug("  Method compressBytes()");
        log.debug("    Initial Image Byte Size - " + data.length);

        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            log.error("Cannot compress Bytes");
        }

        byte[] resultData = outputStream.toByteArray();
        log.debug("    Compressed Image Byte Size - " + resultData.length);
        return resultData;
    }

    // Вспомогательный методы для декомпрессии картинок.
    // Перед тем как возвращать картинку на фронт, будем восстанавливать её состояние
    private static byte[] decompressBytes(byte[] data) {
        log.debug("");
        log.debug("  Method decompressBytes()");
        log.debug("    Initial Size - " + data.length);

        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException e) {
            log.error("Cannot decompress Bytes");
        }

        byte[] decompress = outputStream.toByteArray();
        log.debug("    Decompress Size - " + decompress.length);
        return decompress;
    }
}
