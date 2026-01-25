package com.owl.chat_service.application.service.user.message;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import com.owl.chat_service.application.service.admin.message.ControlMessageAdminServices;
import com.owl.chat_service.application.service.user.chat.GetChatUserServices;
import com.owl.chat_service.persistence.mongodb.document.Chat;
import com.owl.chat_service.persistence.mongodb.document.Message;
import com.owl.chat_service.persistence.mongodb.repository.MessageRepository;
import com.owl.chat_service.presentation.dto.FileMessageUserRequest;
import com.owl.chat_service.presentation.dto.TextMessageUserRequest;
import com.owl.chat_service.presentation.dto.admin.FileMessageAdminRequest;
import com.owl.chat_service.presentation.dto.admin.TextMessageAdminRequest;

@Service
@Transactional
public class ControlMessageUserServices {
    private final GetChatUserServices getChatUserServices;
    private final GetMessageUserServices getMessageUserServices;
    private final ControlMessageAdminServices controlMessageAdminServices;

    public ControlMessageUserServices(GetChatUserServices getChatUserServices, MessageRepository messageRepository, GetMessageUserServices getMessageUserServices, ControlMessageAdminServices controlMessageAdminServices) {
        this.getChatUserServices = getChatUserServices;
        this.getMessageUserServices = getMessageUserServices;
        this.controlMessageAdminServices = controlMessageAdminServices;}

    public Message addNewTextMessage(String requesterId, TextMessageUserRequest textMessageRequest) {
        Chat chat = getChatUserServices.getChatById(requesterId, textMessageRequest.chatId);
        if (chat == null)
            throw new IllegalArgumentException("Chat not found");

        if (!chat.getStatus())
            throw new IllegalArgumentException("Chat have been removed");

        TextMessageAdminRequest request = new TextMessageAdminRequest();
        request.chatId = textMessageRequest.chatId;
        request.content = textMessageRequest.content;
        request.senderId = requesterId;

        return controlMessageAdminServices.addNewTextMessage(request);
    }

    public Message editTextMessage(String requesterId, String messageId, String content) {
        Message existingMessage = getMessageUserServices.getMessageById(requesterId, messageId);

        if (existingMessage == null)
            throw new IllegalArgumentException("Message not found");

        if (!Objects.equals(existingMessage.getSenderId(), requesterId)) 
            throw new SecurityException("Requester does not have permission to edit this message");

        return controlMessageAdminServices.editTextMessage(messageId, content);
    }

    public void softDeleteMessage(String requesterId, String messageId) {
        Message existingMessage = getMessageUserServices.getMessageById(requesterId, messageId);

        if (existingMessage == null)
            throw new IllegalArgumentException("Message not found");

        if (!Objects.equals(existingMessage.getSenderId(), requesterId)) 
            throw new SecurityException("Requester does not have permission to delete this message");

        controlMessageAdminServices.softDeleteMessage(messageId);
    }

    public Message addNewFileMessage(String requesterId, FileMessageUserRequest fileMessageRequest) {
        Chat chat = getChatUserServices.getChatById(requesterId, fileMessageRequest.chatId);
        if (chat == null)
            throw new IllegalArgumentException("Chat not found");

        if (!chat.getStatus())
            throw new IllegalArgumentException("Chat have been removed");

        FileMessageAdminRequest request = new FileMessageAdminRequest();
        request.chatId = fileMessageRequest.chatId;
        request.type = fileMessageRequest.type;
        request.file = fileMessageRequest.file;
        request.senderId = requesterId;

        return controlMessageAdminServices.addNewFileMessage(request);
    }
}
