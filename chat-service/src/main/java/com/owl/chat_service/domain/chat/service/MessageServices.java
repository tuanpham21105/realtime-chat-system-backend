package com.owl.chat_service.domain.chat.service;

import java.nio.file.Path;

import org.springframework.web.multipart.MultipartFile;

import com.owl.chat_service.infrastructure.config.MessageServicesConfig;
import com.owl.chat_service.infrastructure.utils.FileUtils;
import com.owl.chat_service.persistence.mongodb.document.Message.MessageType;
import com.owl.chat_service.presentation.dto.ResourceData;

public class MessageServices {
    public static String saveMessageFile(MessageType type, MultipartFile file) {
        Path path;

        switch (type) {
            case MessageType.IMG:
                path = MessageServicesConfig.IMG_STORAGE;
                break;
            case MessageType.VID:
                path = MessageServicesConfig.VID_STORAGE;
                break;
            default:
                path = MessageServicesConfig.FILE_STORAGE;
                break;
        }
        return FileUtils.saveFile(file, path);
    }
    
    public static ResourceData loadMessageFile(MessageType type, String filename) {
        Path path;

        switch (type) {
            case MessageType.IMG:
                path = MessageServicesConfig.IMG_STORAGE;
                break;
            case MessageType.VID:
                path = MessageServicesConfig.VID_STORAGE;
                break;
            default:
                path = MessageServicesConfig.FILE_STORAGE;
                break;
        }

        return FileUtils.getResourceData(path.resolve(filename));
    }

    public static void deleteMessageFile(MessageType type, String fileName) {
        Path path;

        switch (type) {
            case MessageType.IMG:
                path = MessageServicesConfig.IMG_STORAGE;
                break;
            case MessageType.VID:
                path = MessageServicesConfig.VID_STORAGE;
                break;
            default:
                path = MessageServicesConfig.FILE_STORAGE;
                break;
        }

        FileUtils.deleteFile(path.resolve(fileName));
    }
}
