package com.owl.chat_service.presentation.dto;

import org.springframework.web.multipart.MultipartFile;

public class FileMessageUserRequest {
    public String chatId;
    public String type;
    public MultipartFile file;
}
