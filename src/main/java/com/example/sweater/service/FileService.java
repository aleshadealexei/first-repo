package com.example.sweater.service;

import com.example.sweater.domain.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileService {
    @Value("${upload.path}")
    private static String uploadPath;
    public static void uploadAndSaveFile(Message message,
                                  MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidfile = UUID.randomUUID().toString();
            String resultFileName = uuidfile +  "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFileName));

            message.setFilename(resultFileName);
        }
    }
}
