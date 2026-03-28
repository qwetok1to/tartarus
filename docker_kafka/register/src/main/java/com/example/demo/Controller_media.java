package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;




@RestController
public class Controller_media {
    private static final Logger log = LoggerFactory.getLogger(Controller_media.class);

    @Autowired
    Servise servise;
    @Autowired
    JWT_UTILS jwtUtil;

    @PostMapping("/register")
    public String register(@RequestBody DTO_register dto) {
        return servise.registerUser(dto.username, dto.password);
    }
    @PostMapping("/entrance")
    public String postMethodName(@RequestBody DTO_register dto) {
        return servise.loginUser(dto.username, dto.password);
      
    }
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String authHeader) {
       
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtil.validateToken(token)) {
                System.out.println("Invalid token: " + token);
                 jwtUtil.validateToken(token);
                return ResponseEntity.status(401).body("Unauthorized");
            }

            String username = jwtUtil.getUsernameFromToken(token);
            log.info("User {} is uploading a file", username);

            String fileName = servise.savephoto(file);
            return ResponseEntity.ok("File uploaded successfully: " + fileName);
        
    }
     @PostMapping(value = "/test", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String test(@RequestParam("file") MultipartFile file){
        servise.savephoto(file);
        
        return "File received: " + file.getOriginalFilename();
    }

     
  
    
}

