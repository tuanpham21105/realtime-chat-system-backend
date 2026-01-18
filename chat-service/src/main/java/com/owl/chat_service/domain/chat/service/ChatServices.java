package com.owl.chat_service.domain.chat.service;

import java.nio.file.Path;
import org.springframework.web.multipart.MultipartFile;

import com.owl.chat_service.infrastructure.config.ChatServicesConfig;
import com.owl.chat_service.infrastructure.utils.FileUtils;
import com.owl.chat_service.presentation.dto.ResourceData;

public class ChatServices {
    public static String saveChatAvatarFile(MultipartFile file) {
        return FileUtils.saveFile(file, ChatServicesConfig.AVATAR_STORAGE);
    }
    
    public static ResourceData loadAvatar(String avatar) {
        Path filePath = ChatServicesConfig.AVATAR_STORAGE.resolve(avatar);

        return FileUtils.getResourceData(filePath);
    }

    public static void deleteFile(String fileName) {
        FileUtils.deleteFile(ChatServicesConfig.AVATAR_STORAGE.resolve(fileName));
    }
}
