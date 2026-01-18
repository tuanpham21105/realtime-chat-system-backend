package com.owl.chat_service.domain.chat.validate;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;

import com.owl.chat_service.infrastructure.config.ChatServicesConfig;
import com.owl.chat_service.persistence.mongodb.document.Chat.ChatType;

public class ChatValidate {
    public static boolean validateType(String type) {
        if (type == null || type.isBlank()) {
            return false;
        }

        try {
            ChatType.valueOf(type.trim().toUpperCase());
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static boolean validateName(String name) {
        if (name == null || name.isBlank())
            return false;

        return true;
    }

    public static boolean validateChatId(String chatId) {
        if (chatId == null || chatId.isBlank())
            return false;

        return true;
    }
    public static void validateAvatarFileType(MultipartFile file) {
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new IllegalArgumentException("Invalid image content");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to read image file");
        }
    }

    public static void validateAvatarFileMetaData(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File is not an image");
        }

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }

        if (file.getSize() > ChatServicesConfig.MAX_SIZE) {
            throw new IllegalArgumentException("Image must be smaller than 10MB");
        }
    }
}
