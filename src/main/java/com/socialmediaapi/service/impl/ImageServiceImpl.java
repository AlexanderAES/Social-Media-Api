package com.socialmediaapi.service.impl;

import com.socialmediaapi.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${upload.path}")
    private String uploadPath;

    /**
     * Метод использутся для сохранения загруженного файла.
     *
     * @param file сохраяняемый файл.
     * @return String имя сохраненного файла.
     * @throws IOException
     */
    public String saveFile(MultipartFile file) throws IOException {
        String resultFilename = "";
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            String uuidFile = UUID.randomUUID().toString();
            resultFilename = uuidFile + file.getOriginalFilename();
            String absolutePath = uploadDir.getAbsolutePath();
            file.transferTo(new File(absolutePath + "/" + resultFilename));
        }
        return resultFilename;
    }
}
