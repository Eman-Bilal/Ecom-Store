package com.example.EcomStore.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
public class FileStorageService {

  @Value("${app.upload.dir}")
  private String uploadDir;

  public String storeFile(MultipartFile file) {

    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException("Image file is empty.");
    }

    String contentType = file.getContentType();

    if (contentType == null ||
        !(contentType.equals("image/jpeg")
            || contentType.equals("image/png")
            || contentType.equals("image/webp"))) {

      throw new IllegalArgumentException("Only JPG, PNG and WEBP images are allowed.");
    }
    try {
      Path uploadPath = Paths.get(uploadDir);
      if (!Files.exists(uploadPath)) {
        Files.createDirectories(uploadPath);
      }

      String originalFilename = file.getOriginalFilename();
      String extension = (originalFilename != null && originalFilename.contains("."))
          ? originalFilename.substring(originalFilename.lastIndexOf("."))
          : "";
      String uniqueFilename = UUID.randomUUID() + extension;

      Path targetPath = uploadPath.resolve(uniqueFilename);
      Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

      return uploadDir + "/" + uniqueFilename;

    } catch (IOException e) {
      log.error("Failed to store file: {}", e.getMessage());
      throw new RuntimeException("Failed to store file", e);
    }
  }
}