package com.socialmediaapi.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    String saveFile(MultipartFile file) throws IOException;
}
