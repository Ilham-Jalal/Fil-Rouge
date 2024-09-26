package com.example.demo.services;

import com.cloudinary.Cloudinary;
import com.example.demo.exceptions.ImageUploadException; // Import the custom exception
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(@Value("${cloudinary.cloud-name}") String cloudName,
                             @Value("${cloudinary.api-key}") String apiKey,
                             @Value("${cloudinary.api-secret}") String apiSecret) {
        this.cloudinary = new Cloudinary(Map.of(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    public List<String> uploadImages(MultipartFile[] images) {
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile image : images) {
            try {
                Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), new HashMap<>());
                imageUrls.add((String) uploadResult.get("url"));
            } catch (IOException e) {
                throw new ImageUploadException("Error uploading image: " + image.getOriginalFilename(), e);
            } catch (Exception e) {
                throw new ImageUploadException("An unexpected error occurred while uploading image: " + image.getOriginalFilename(), e);
            }
        }
        return imageUrls;
    }
}
