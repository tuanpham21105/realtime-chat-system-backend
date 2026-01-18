package com.owl.chat_service.application.service.admin.chat;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.owl.chat_service.application.service.admin.chat_member.ControlChatMemberAdminSerivces;
import com.owl.chat_service.domain.chat.service.ChatServices;
import com.owl.chat_service.domain.chat.validate.ChatValidate;
import com.owl.chat_service.persistence.mongodb.document.Chat;
import com.owl.chat_service.persistence.mongodb.document.Message;
import com.owl.chat_service.persistence.mongodb.document.Chat.ChatType;
import com.owl.chat_service.persistence.mongodb.repository.ChatRepository;
import com.owl.chat_service.presentation.dto.admin.ChatAdminRequest;

@Service
@Transactional
public class ControlChatAdminServices {

    private final ControlChatMemberAdminSerivces controlChatMemberAdminSerivces;
    private final GetChatAdminServices getChatAdminServices;
    private final ChatRepository chatRepository;

    public ControlChatAdminServices(ChatRepository chatRepository, GetChatAdminServices getChatAdminServices, ControlChatMemberAdminSerivces controlChatMemberAdminSerivces) {
        this.getChatAdminServices = getChatAdminServices;
        this.chatRepository = chatRepository;
        this.controlChatMemberAdminSerivces = controlChatMemberAdminSerivces;}

    public Chat addNewChat(ChatAdminRequest chatRequest) {
        Chat newChat = new Chat();

        String normalizedType = chatRequest.type.trim().toUpperCase();
        if (!ChatValidate.validateType(normalizedType)) 
            throw new IllegalArgumentException("Invalid type");

        if (!ChatValidate.validateName(chatRequest.name)) 
            throw new IllegalArgumentException("Invalid name");

        newChat.setId(UUID.randomUUID().toString());
        newChat.setStatus(true);
        newChat.setType(ChatType.valueOf(normalizedType));
        newChat.setName(chatRequest.name);
        newChat.setInitiatorId(chatRequest.initiatorId);
        newChat.setCreatedDate(Instant.now());
        newChat.setUpdatedDate(newChat.getCreatedDate());

        return chatRepository.save(newChat);
    }

    public Chat updateChat(String chatId, ChatAdminRequest chatRequest) {
        if (!ChatValidate.validateChatId(chatId))
            throw new IllegalArgumentException("Invalid chat id");

        Chat existingChat = getChatAdminServices.getChatById(chatId);

        if (existingChat == null) 
            throw new IllegalArgumentException("Chat does not exists");
        

        String normalizedType = chatRequest.type.trim().toUpperCase();
        if (!ChatValidate.validateType(normalizedType)) 
            throw new IllegalArgumentException("Invalid type");

        if (!ChatValidate.validateName(chatRequest.name)) 
            throw new IllegalArgumentException("Invalid name");

        existingChat.setType(ChatType.valueOf(normalizedType));
        existingChat.setName(chatRequest.name);
        existingChat.setInitiatorId(chatRequest.initiatorId);
        existingChat.setUpdatedDate(Instant.now());

        return chatRepository.save(existingChat);
    }

    public Chat updateChatStatus(String chatId, boolean status) {
        if (!ChatValidate.validateChatId(chatId))
            throw new IllegalArgumentException("Invalid chat id");

        Chat existingChat = getChatAdminServices.getChatById(chatId);

        if (existingChat == null) 
            throw new IllegalArgumentException("Chat does not exists");

        existingChat.setStatus(status);
        existingChat.setUpdatedDate(Instant.now());

        return chatRepository.save(existingChat);
    }

    public void softDeleteChat(String chatId) {
        if (!ChatValidate.validateChatId(chatId))
            throw new IllegalArgumentException("Invalid chat id");

        Chat existingChat = getChatAdminServices.getChatById(chatId);

        if (existingChat == null) 
            throw new IllegalArgumentException("Chat does not exists");

        updateChatStatus(chatId, false);

        controlChatMemberAdminSerivces.deleteChatMemberByChatId(chatId);
    }

    public void deleteChat(String chatId) {
        if (!ChatValidate.validateChatId(chatId))
            throw new IllegalArgumentException("Invalid chat id");

        Chat existingChat = getChatAdminServices.getChatById(chatId);

        if (existingChat == null) 
            throw new IllegalArgumentException("Chat does not exists");

        deleteChatAvatarFile(existingChat.getAvatar());

        chatRepository.deleteById(Objects.requireNonNull(existingChat.getId(), "Delete chat is null"));

        controlChatMemberAdminSerivces.deleteChatMemberByChatId(chatId);
    }

    public void updateChatNewestMessage(Message message) {
        if (message == null) 
            throw new IllegalArgumentException("Invalid message");

        if (message.getId() == null || message.getId().isBlank()) 
            throw new IllegalArgumentException("Invalid message id");

        if (message.getSentDate() == null) 
            throw new IllegalArgumentException("Invalid message sent date");

        if (message.getChatId() == null || message.getChatId().isBlank()) 
            throw new IllegalArgumentException("Invalid chat id");

        Chat chat = getChatAdminServices.getChatById(message.getChatId());

        if (chat == null)
            throw new IllegalArgumentException("Chat does not exists");

        if (chat.getNewestMessageDate() == null || message.getSentDate().compareTo(chat.getNewestMessageDate()) == 1) {
            chat.setNewestMessageId(message.getId());
            chat.setNewestMessageDate(message.getSentDate());
            chatRepository.save(chat);
        }
    }

    public void updateChatAvatarFile(String chatId, MultipartFile file) {
        Chat existingChat = getChatAdminServices.getChatById(chatId);

        if (existingChat == null) {
            throw new IllegalArgumentException("Chat not found");
        }

        ChatValidate.validateAvatarFileMetaData(file);

        ChatValidate.validateAvatarFileType(file);

        String avatar = existingChat.getAvatar();

        if (avatar != null && !avatar.isEmpty()) {
            deleteChatAvatarFile(avatar);
        }

        existingChat.setAvatar(ChatServices.saveChatAvatarFile(file));

        chatRepository.save(existingChat);
    }

    public void deleteChatAvatarFile(String avatarFileName) {
        if (avatarFileName == null || avatarFileName.isEmpty()) {
            throw new IllegalArgumentException("Chat does not have avatar");
        }

        ChatServices.deleteFile(avatarFileName);
    }
}
