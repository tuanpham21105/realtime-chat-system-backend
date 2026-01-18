package com.owl.chat_service.infrastructure.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ChatServicesConfig {
    public static final long MAX_SIZE = 10 * 1024 * 1024;

    public static final Path AVATAR_STORAGE = Paths.get("resources/chat_avatar");
}
