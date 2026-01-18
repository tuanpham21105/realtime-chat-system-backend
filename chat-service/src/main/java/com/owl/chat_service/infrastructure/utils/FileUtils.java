package com.owl.chat_service.infrastructure.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import com.owl.chat_service.infrastructure.config.ChatServicesConfig;
import com.owl.chat_service.infrastructure.config.MessageServicesConfig;
import com.owl.chat_service.presentation.dto.ResourceData;

public class FileUtils {
    public static String getFileExtension(String filename) {
        if (filename == null) return "";
        int lastDot = filename.lastIndexOf('.');
        return (lastDot == -1) ? "" : filename.substring(lastDot);
    }

    public static Path getAvatarFilePath(String avatarString) {
        return ChatServicesConfig.AVATAR_STORAGE.resolve(avatarString);
    }

    public static ResourceData getResourceData(Path filePath) {
        ResourceData data = new ResourceData();

        try {
            data.contentType = Files.probeContentType(filePath);
        }
        catch (IOException e) {
            throw new RuntimeException("Cannot get content type", e);
        }

        try {

            Resource resource = new UrlResource(Objects.requireNonNull(filePath.toUri(), "Resource url is null"));
            if (!resource.exists()) {
                throw new IllegalArgumentException("File not found");
            }
            data.resource = resource;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to load file", e);
        }

        return data;
    }

    public static String saveFile(MultipartFile file, Path path) {
        try {
            Files.createDirectories(path);

            String extension = FileUtils.getFileExtension(file.getOriginalFilename());

            String storedName = UUID.randomUUID().toString() + extension;

            Path target = path.resolve(storedName);

            Files.copy(file.getInputStream(), target);

            return storedName;
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public static void deleteFile(Path target) {
        if (Files.exists(target)) {
            try {
                Files.delete(target);
            }
            catch (IOException e) {
                throw new RuntimeException("Failed to delete avatar file", e);
            }
        }
        else {
            throw new IllegalArgumentException("User profile avatar not found");
        }
    }

    public static boolean isImage(String mimeType) {
        return MessageServicesConfig.IMAGE_TYPES.contains(mimeType);
    }

    public static boolean isVideo(String mimeType) {
        return MessageServicesConfig.VIDEO_TYPES.contains(mimeType);
    }

    public static boolean isGenericFile(String mimeType) {
        return !mimeType.startsWith("image/")
                && !mimeType.startsWith("video/");
    }
}
