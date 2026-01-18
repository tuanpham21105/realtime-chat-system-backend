package com.owl.chat_service.infrastructure.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class MessageServicesConfig {
    public static final Path IMG_STORAGE = Paths.get("resources/message_img");

    public static final Path VID_STORAGE = Paths.get("resources/message_vid");

    public static final Path FILE_STORAGE = Paths.get("resources/message_file");
    
    public static final long MAX_SIZE = 10 * 1024 * 1024;

    public static final Set<String> IMAGE_TYPES = Set.of(
            "image/png",
            "image/jpeg",
            "image/webp",
            "image/gif"
    );

    public static final Set<String> VIDEO_TYPES = Set.of(
            "video/mp4",
            "video/webm",
            "video/quicktime"
    );
}
