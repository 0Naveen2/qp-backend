package PreviousYear.questions.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path uploadDir;
    private final long MAX_FILE_SIZE = 20 * 1024 * 1024; // 20 MB

    public FileStorageService(@Value("${file.upload-dir:uploads}") String uploadDir) {
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    public String storeFile(MultipartFile file) {
        try {
            System.out.println("Storing file: " + file.getOriginalFilename());

            if (file.getSize() > MAX_FILE_SIZE) {
                throw new RuntimeException("File size exceeds 5MB limit");
            }

            String original = file.getOriginalFilename() == null ? "file.pdf" : file.getOriginalFilename();
            String ext = original.contains(".") ? original.substring(original.lastIndexOf('.')) : ".pdf";
            String storedName = UUID.randomUUID().toString() + ext;
            Path target = this.uploadDir.resolve(storedName).normalize();

            System.out.println("Resolved target path: " + target.toString());

            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("File stored successfully with name: " + storedName);

            return storedName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public Resource loadAsResource(String storedFileName) {
        try {
            Path file = this.uploadDir.resolve(storedFileName).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File not found or not readable: " + storedFileName);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("File not found: " + storedFileName, e);
        }
    }

    public boolean deleteFile(String storedFileName) {
        try {
            Path file = this.uploadDir.resolve(storedFileName).normalize();
            return Files.deleteIfExists(file);
        } catch (IOException e) {
            return false;
        }
    }
}
