package com.owl.chat_service.domain.chat.validate;

import java.io.IOException;

import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import com.owl.chat_service.infrastructure.config.MessageServicesConfig;
import com.owl.chat_service.infrastructure.utils.FileUtils;
import com.owl.chat_service.persistence.mongodb.document.Message.MessageState;
import com.owl.chat_service.persistence.mongodb.document.Message.MessageType;

public class MessageValidate {
    public static boolean ValidateType(String type) {
        try {
            MessageType.valueOf(type);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public static boolean ValidateState(String state) {
        try {
            MessageState.valueOf(state);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public static boolean validateSenderId(String senderId) {
        if (senderId == null || senderId.isBlank()) {
            return false;
        }
        return true;
    }

    public static boolean validateChatId(String chatId) {
        if (chatId == null || chatId.isBlank()) {
            return false;
        }
        return true;
    }

    public static boolean validateContent(String content) {
        if (content == null || content.isBlank()) {
            return false;
        }
        return true;
    }

    private static final Tika TIKA = new Tika();

    public static MessageType validateFileMetaData(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be null or empty");
        }

        if (file.getSize() > MessageServicesConfig.MAX_SIZE) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size");
        }

        String detectedType;
        try {
            detectedType = TIKA.detect(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read file content", e);
        }

        if (FileUtils.isImage(detectedType)) {
            return MessageType.IMG;
        }

        if (FileUtils.isVideo(detectedType)) {
            return MessageType.VID;
        }

        if (FileUtils.isGenericFile(detectedType)) {
            return MessageType.GENERIC_FILE;
        }

        throw new IllegalArgumentException("Unsupported file type: " + detectedType);
    }
}
