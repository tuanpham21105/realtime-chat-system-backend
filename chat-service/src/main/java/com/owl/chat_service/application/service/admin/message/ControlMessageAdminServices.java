package com.owl.chat_service.application.service.admin.message;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.owl.chat_service.application.service.admin.chat.ControlChatAdminServices;
import com.owl.chat_service.application.service.admin.chat.GetChatAdminServices;
import com.owl.chat_service.application.service.admin.chat_member.GetChatMemberAdminServices;
import com.owl.chat_service.domain.chat.service.ChatMemberServices;
import com.owl.chat_service.domain.chat.service.MessageServices;
import com.owl.chat_service.domain.chat.validate.MessageValidate;
import com.owl.chat_service.external_service.client.UserServiceApiClient;
import com.owl.chat_service.persistence.mongodb.document.Chat;
import com.owl.chat_service.persistence.mongodb.document.ChatMember;
import com.owl.chat_service.persistence.mongodb.document.Message;
import com.owl.chat_service.persistence.mongodb.document.ChatMember.ChatMemberRole;
import com.owl.chat_service.persistence.mongodb.document.Message.MessageState;
import com.owl.chat_service.persistence.mongodb.document.Message.MessageType;
import com.owl.chat_service.persistence.mongodb.repository.MessageRepository;
import com.owl.chat_service.presentation.dto.admin.FileMessageAdminRequest;
import com.owl.chat_service.presentation.dto.admin.TextMessageAdminRequest;

@Service
@Transactional
public class ControlMessageAdminServices {
    private final MessageRepository messageRepository;
    private final GetChatAdminServices getChatAdminServices;
    private final GetMessageAdminServices getMessageAdminServices;
    private final ControlChatAdminServices controlChatAdminService;
    private final GetChatMemberAdminServices getChatMemberAdminServices;
    private final UserServiceApiClient userServiceApiClient;

    public ControlMessageAdminServices(MessageRepository messageRepository, GetChatAdminServices getChatAdminServices, GetMessageAdminServices getMessageAdminServices, ControlChatAdminServices controlChatAdminService, GetChatMemberAdminServices getChatMemberAdminServices, UserServiceApiClient userServiceApiClient) {
        this.messageRepository = messageRepository;
        this.getChatAdminServices = getChatAdminServices;
        this.getMessageAdminServices = getMessageAdminServices;
        this.controlChatAdminService = controlChatAdminService;
        this.getChatMemberAdminServices = getChatMemberAdminServices;
        this.userServiceApiClient = userServiceApiClient;
    }

    public Message addNewTextMessage(TextMessageAdminRequest textMessageRequest) {
        if (!MessageValidate.validateSenderId(textMessageRequest.senderId)) {
            throw new IllegalArgumentException("Invalid sender id");
        }

        try {
            if (userServiceApiClient.getUserById(textMessageRequest.senderId) == null) 
                throw new IllegalArgumentException("Sender not found");
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        if (!MessageValidate.validateChatId(textMessageRequest.chatId)) {
            throw new IllegalArgumentException("Invalid chat id");
        }

        Chat exitstingChat = getChatAdminServices.getChatById(textMessageRequest.chatId);
        if (exitstingChat == null) {
            throw new IllegalArgumentException("Chat does not exists");
        }

        if (!exitstingChat.getStatus())
            throw new IllegalArgumentException("Chat have been removed");

        ChatMember chatMember = getChatMemberAdminServices.getChatMemberByChatIdAndMemberId(textMessageRequest.chatId, textMessageRequest.senderId);
        if (chatMember == null) 
            throw new SecurityException("Sender does not have permission to access this chat");

        if (ChatMemberServices.compareRole(chatMember.getRole(), ChatMemberRole.MEMBER) < 0)
            throw new SecurityException("Sender does not have permission to send message in this chat");

        if (!MessageValidate.validateContent(textMessageRequest.content)) {
            throw new IllegalArgumentException("Invalid content");
        }
        
        Message newMessage = new Message();
        newMessage.setId(UUID.randomUUID().toString());
        newMessage.setChatId(textMessageRequest.chatId);
        newMessage.setStatus(true);
        newMessage.setState(MessageState.ORIGIN);
        newMessage.setType(MessageType.TEXT);
        newMessage.setContent(textMessageRequest.content);
        newMessage.setSenderId(textMessageRequest.senderId);
        newMessage.setSentDate(Instant.now());
        newMessage.setCreatedDate(newMessage.getSentDate());

        messageRepository.save(newMessage);

        controlChatAdminService.updateChatNewestMessage(newMessage);

        return newMessage;
    }

    public Message editTextMessage(String messageId, String content) {
        Message existingMessage = getMessageAdminServices.getMessageById(messageId);

        if (existingMessage == null) {
            throw new IllegalArgumentException("Message not found");
        }

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Content cannot be null");
        }

        existingMessage.setState(MessageState.EDITED);
        messageRepository.save(existingMessage);

        Message newMessage = new Message();
        newMessage.setId(UUID.randomUUID().toString());
        newMessage.setChatId(existingMessage.getChatId());
        newMessage.setStatus(true);
        newMessage.setState(MessageState.ORIGIN);
        newMessage.setType(MessageType.TEXT);
        newMessage.setContent(content);
        newMessage.setSenderId(existingMessage.getSenderId());
        newMessage.setPredecessorId(existingMessage.getId());
        newMessage.setSentDate(existingMessage.getSentDate());
        newMessage.setCreatedDate(Instant.now());

        messageRepository.save(newMessage);

        controlChatAdminService.updateChatNewestMessage(newMessage);

        return newMessage;
    }

    public Message activateMessage(String messageId) {
        Message existingMessage = getMessageAdminServices.getMessageById(messageId);

        if (existingMessage == null) {
            throw new IllegalArgumentException("Message not found");
        }

        existingMessage.setStatus(true);
        existingMessage.setState(MessageState.ORIGIN);
        existingMessage.setRemovedDate(null);
        

        return messageRepository.save(existingMessage);
    }

    public void softDeleteMessage(String messageId) {
        Message existingMessage = getMessageAdminServices.getMessageById(messageId);

        if (existingMessage == null) {
            throw new IllegalArgumentException("Message not found");
        }

        switch (existingMessage.getState()) {
            case MessageState.EDITED:
                throw new IllegalArgumentException("Cannot remove edited message");
            case MessageState.REMOVED:
                throw new IllegalArgumentException("Message already removed");
            default:
                break;
        }

        existingMessage.setStatus(false);
        existingMessage.setState(MessageState.REMOVED);
        existingMessage.setRemovedDate(Instant.now());

        messageRepository.save(existingMessage);

        controlChatAdminService.updateChatNewestMessage(existingMessage);
    }

    public void hardDeleteMessage(String messageId) {
        Message existingMessage = getMessageAdminServices.getMessageById(messageId);

        if (existingMessage == null) {
            throw new IllegalArgumentException("Message not found");
        }

        MessageType type = existingMessage.getType();
        if (type == MessageType.IMG || type == MessageType.VID || type == MessageType.GENERIC_FILE) {
            MessageServices.deleteMessageFile(type, existingMessage.getContent());
        }

        messageRepository.deleteById(Objects.requireNonNull(messageId, "Message id cannot be null"));
    }

    public Message addNewFileMessage(FileMessageAdminRequest fileMessageRequest) {
        if (!MessageValidate.validateSenderId(fileMessageRequest.senderId)) {
            throw new IllegalArgumentException("Invalid sender id");
        }

        if (!MessageValidate.validateChatId(fileMessageRequest.chatId)) {
            throw new IllegalArgumentException("Invalid chat id");
        }

        Chat exitstingChat = getChatAdminServices.getChatById(fileMessageRequest.chatId);
        if (exitstingChat == null) {
            throw new IllegalArgumentException("Chat does not exists");
        }

        if (!exitstingChat.getStatus())
            throw new IllegalArgumentException("Chat have been removed");

        ChatMember chatMember = getChatMemberAdminServices.getChatMemberByChatIdAndMemberId(fileMessageRequest.chatId, fileMessageRequest.senderId);
        if (chatMember == null) 
            throw new SecurityException("Sender does not have permission to access this chat");

        if (ChatMemberServices.compareRole(chatMember.getRole(), ChatMemberRole.MEMBER) < 0)
            throw new SecurityException("Sender does not have permission to send message in this chat");

        if (!MessageValidate.ValidateType(fileMessageRequest.type))
            throw new IllegalArgumentException("Invalid type");

        MessageType type = MessageValidate.validateFileMetaData(fileMessageRequest.file);
        if (type != MessageType.valueOf(fileMessageRequest.type)) {
            throw new IllegalArgumentException("Message type and file type do not match");
        }
        
        Message newMessage = new Message();
        newMessage.setId(UUID.randomUUID().toString());
        newMessage.setChatId(fileMessageRequest.chatId);
        newMessage.setStatus(true);
        newMessage.setState(MessageState.ORIGIN);
        newMessage.setType(type);
        newMessage.setSenderId(fileMessageRequest.senderId);
        newMessage.setSentDate(Instant.now());
        newMessage.setCreatedDate(newMessage.getSentDate());

        newMessage.setContent(MessageServices.saveMessageFile(type, fileMessageRequest.file));

        messageRepository.save(newMessage);

        controlChatAdminService.updateChatNewestMessage(newMessage);

        return newMessage;
    }
}
