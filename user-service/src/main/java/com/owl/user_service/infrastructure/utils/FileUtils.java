package com.owl.user_service.infrastructure.utils;

import java.nio.file.Path;
import com.owl.user_service.infrastructure.config.UserProfileServicesConfig;

public class FileUtils {
    public static String getFileExtension(String filename) {
        if (filename == null) return "";
        int lastDot = filename.lastIndexOf('.');
        return (lastDot == -1) ? "" : filename.substring(lastDot);
    }

    public static Path getAvatarFilePath(String avatarString) {
        return UserProfileServicesConfig.AVATAR_STORAGE.resolve(avatarString);
    }

}
