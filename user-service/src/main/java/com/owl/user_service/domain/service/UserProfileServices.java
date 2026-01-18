package com.owl.user_service.domain.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import com.owl.user_service.infrastructure.config.UserProfileServicesConfig;
import com.owl.user_service.infrastructure.utils.FileUtils;
import com.owl.user_service.persistence.jpa.entity.Account;
import com.owl.user_service.persistence.jpa.entity.UserProfile;
import com.owl.user_service.presentation.dto.request.UserProfileRequest;

public class UserProfileServices {
    public boolean ValidateName(String name) {
        if (name == null || name.isBlank()) {
            return false;
        }

        return true;
    }

    public boolean ValidateEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }

        if (!UserProfileServicesConfig.EMAIL_PATTERN.matcher(email).matches()) {
            return false;
        }

        return true;
    }

    public boolean ValidatePhoneNumber(String phone) {
        if (phone == null || phone.isBlank()) {
            return false;
        }

        if (!UserProfileServicesConfig.PHONE_PATTERN.matcher(phone).matches()) {
            return false;
        }

        return true;
    }

    public UserProfile CreateNewUserProfile(Account account, UserProfileRequest userProfileRequest) {
        if (!ValidateName(userProfileRequest.getName())) {
            throw new IllegalArgumentException("Invalid name");
        }

        if (!ValidateEmail(userProfileRequest.getEmail())) {
            throw new IllegalArgumentException("Invalid email");
        }

        if (!ValidatePhoneNumber(userProfileRequest.getPhoneNumber())) {
            throw new IllegalArgumentException("Invalid phone number");
        }

        UserProfile newUserProfile = new UserProfile(
            account,
            userProfileRequest.getName(),
            userProfileRequest.getGender(),
            userProfileRequest.getDateOfBirth(),
            null,
            userProfileRequest.getEmail(),
            userProfileRequest.getPhoneNumber()
        );

        return newUserProfile;
    }

    public void ValidateAvatarFileType(MultipartFile file) {
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new IllegalArgumentException("Invalid image content");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to read image file");
        }
    }

    public void ValidateAvatarFileMetaData(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File is not an image");
        }

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }

        if (file.getSize() > UserProfileServicesConfig.MAX_SIZE) {
            throw new IllegalArgumentException("Image must be smaller than 10MB");
        }
    }

    public String SaveUserAvatarFile(String id, MultipartFile file) {
        try {
            Files.createDirectories(UserProfileServicesConfig.AVATAR_STORAGE);

            String extension = FileUtils.getFileExtension(file.getOriginalFilename());

            String storedName = UUID.randomUUID().toString() + extension;

            Path target = UserProfileServicesConfig.AVATAR_STORAGE.resolve(storedName);

            Files.copy(file.getInputStream(), target);

            return storedName;
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to store avatar file", e);
        }
    }
    
    public Resource loadAvatar(String avatar) {
        try {
            Path filePath = UserProfileServicesConfig.AVATAR_STORAGE.resolve(avatar);

            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                throw new IllegalArgumentException("File not found");
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to load file", e);
        }
    }

}
