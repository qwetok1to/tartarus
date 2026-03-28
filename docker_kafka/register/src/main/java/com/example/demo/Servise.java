package com.example.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class Servise {
    private final UserRep userRepository;
    private final JWT_UTILS jwtUtils;
    private final Path filePath;

    public Servise(UserRep userRepository, JWT_UTILS jwtUtils, @Value("${app.media-dir:media_data}") String mediaDir) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.filePath = Paths.get(mediaDir).toAbsolutePath().normalize();
    }

    public String registerUser(String name, String password) {
        if (userRepository.existsByName(name)) {
            throw new IllegalArgumentException("User already exists");
        }

        UserEntiy entity = new UserEntiy();
        entity.setName(name);
        entity.setPassword(password);

        userRepository.save(entity);
        return jwtUtils.generalToken(name);
    }

    public String loginUser(String name, String password) {
        UserEntiy user = userRepository.findByName(name);
        if (user == null || !user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return jwtUtils.generalToken(name);
    }

    public DTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(u -> new DTO(u.getName(), u.getPassword()))
                .orElse(null);
    }

  public List<DTO> listUsers() {
    return StreamSupport.stream(userRepository.findAll().spliterator(), false)
            .map(u -> new DTO(u.getName(), u.getPassword()))
            .collect(java.util.stream.Collectors.toList());
}

    public void editProfile(String username){
        UserEntiy user = userRepository.findByName(username);

    }   

    public String savephoto(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String contentType = file.getContentType();
        if (contentType == null) {
            throw new IllegalArgumentException("Unknown file type");
        }

        boolean isImage = contentType.equals("image/jpeg") || contentType.equals("image/png");
        boolean isPdf = contentType.equals("application/pdf");
        if (!isImage && !isPdf) {
            throw new IllegalArgumentException("Only JPG, PNG, PDF are allowed");
        }

        try {
            Files.createDirectories(filePath);

            String originalFilename = file.getOriginalFilename();
            String extension = ".bin";
            if (originalFilename != null && originalFilename.lastIndexOf('.') >= 0) {
                extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            }

            String fileName = UUID.randomUUID() + extension;
            Path targetPath = filePath.resolve(fileName).normalize();

            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File saved to: " + targetPath);
            return targetPath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }
    }
}


