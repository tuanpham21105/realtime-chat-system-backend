package com.chat_system.api_gateway.presentation.dto.request.message;

import org.springframework.web.multipart.MultipartFile;

public class FileMessageUserRequest {
    public String chatId;
    public String type;
    public MultipartFile file;
}
