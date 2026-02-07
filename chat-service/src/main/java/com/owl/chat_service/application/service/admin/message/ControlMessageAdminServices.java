package com.owl.chat_service.application.service.admin.message;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.owl.chat_service.application.service.admin.chat.GetChatAdminServices;
import com.owl.chat_service.application.service.admin.chat_member.GetChatMemberAdminServices;
import com.owl.chat_service.application.service.event.AddMessageEvent;
import com.owl.chat_service.application.service.event.EventEmitter;
import com.owl.chat_service.domain.chat.service.ChatMemberServices;
import com.owl.chat_service.domain.chat.service.MessageServices;
import com.owl.chat_service.domain.chat.validate.MessageValidate;
import com.owl.chat_service.external_service.client.BlockUserServiceApiClient;
import com.owl.chat_service.external_service.client.UserServiceApiClient;
import com.owl.chat_service.persistence.mongodb.document.Chat;
import com.owl.chat_service.persistence.mongodb.document.ChatMember;
import com.owl.chat_service.persistence.mongodb.document.Message;
import com.owl.chat_service.persistence.mongodb.document.Chat.ChatType;
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
    private final GetChatMemberAdminServices getChatMemberAdminServices;
    private final UserServiceApiClient userServiceApiClient;
    private final BlockUserServiceApiClient blockUserServiceApiClient;
    private final EventEmitter emitter;


    public ControlMessageAdminServices(MessageRepository messageRepository, GetChatAdminServices getChatAdminServices, GetMessageAdminServices getMessageAdminServices, GetChatMemberAdminServices getChatMemberAdminServices, UserServiceApiClient userServiceApiClient, BlockUserServiceApiClient blockUserServiceApiClient, EventEmitter emitter) {
        this.messageRepository = messageRepository;
        this.getChatAdminServices = getChatAdminServices;
        this.getMessageAdminServices = getMessageAdminServices;
        this.getChatMemberAdminServices = getChatMemberAdminServices;
        this.userServiceApiClient = userServiceApiClient;
        this.blockUserServiceApiClient = blockUserServiceApiClient;
        this.emitter = emitter;
    }

    public Message addNewTextMessage(TextMessageAdminRequest textMessageRequest) {
        if (!MessageValidate.validateSenderId(textMessageRequest.senderId)) {
            throw new IllegalArgumentException("Invalid sender id");
        }

        if (userServiceApiClient.getUserById(textMessageRequest.senderId) == null) 
            throw new IllegalArgumentException("Sender not found");

        if (!MessageValidate.validateChatId(textMessageRequest.chatId)) {
            throw new IllegalArgumentException("Invalid chat id");
        }

        Chat existingChat = getChatAdminServices.getChatById(textMessageRequest.chatId);
        if (existingChat == null) {
            throw new IllegalArgumentException("Chat does not exists");
        }

        if (!existingChat.getStatus())
            throw new IllegalArgumentException("Chat have been removed");

        ChatMember chatMember = getChatMemberAdminServices.getChatMemberByChatIdAndMemberId(textMessageRequest.chatId, textMessageRequest.senderId);
        if (chatMember == null) 
            throw new SecurityException("Sender does not have permission to access this chat");

        if (ChatMemberServices.compareRole(chatMember.getRole(), ChatMemberRole.MEMBER) < 0)
            throw new SecurityException("Sender does not have permission to send message in this chat");

        if (!MessageValidate.validateContent(textMessageRequest.content)) {
            throw new IllegalArgumentException("Invalid content");
        }

        if (existingChat.getType() == ChatType.PRIVATE) {
            List<ChatMember> chatMembers = getChatMemberAdminServices.getChatMembersByChatId(existingChat.getId(), -1, 10, false);

            if (chatMembers == null || chatMembers.size() < 2) {
                throw new IllegalArgumentException("Private chat must have two members");
            }

            String userA = chatMembers.get(0).getMemberId();
            String userB = chatMembers.get(1).getMemberId();

            if (blockUserServiceApiClient.getUserBlockUser(userA, userB) != null || blockUserServiceApiClient.getUserBlockUser(userB, userA) != null) {
                throw new IllegalArgumentException("Chat members have blocked each other");
            }
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

        AddMessageEvent event = new AddMessageEvent();
        event.setMessage(newMessage);

        emitter.emit(event);

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

        Chat existingChat = getChatAdminServices.getChatById(existingMessage.getChatId());

        if (existingChat.getType() == ChatType.PRIVATE) {
            List<ChatMember> chatMembers = getChatMemberAdminServices.getChatMembersByChatId(existingChat.getId(), -1, 10, false);

            if (chatMembers == null || chatMembers.size() < 2) {
                throw new IllegalArgumentException("Private chat must have two members");
            }

            String userA = chatMembers.get(0).getMemberId();
            String userB = chatMembers.get(1).getMemberId();

            if (blockUserServiceApiClient.getUserBlockUser(userA, userB) != null || blockUserServiceApiClient.getUserBlockUser(userB, userA) != null) {
                throw new IllegalArgumentException("Chat members have blocked each other");
            }
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

        AddMessageEvent event = new AddMessageEvent();
        event.setMessage(newMessage);

        emitter.emit(event);

        return newMessage;
    }

    public Message activateMessage(String messageId) {
        Message existingMessage = getMessageAdminServices.getMessageById(messageId);

        if (existingMessage == null) {
            throw new IllegalArgumentException("Message not found");
        }

        Chat existingChat = getChatAdminServices.getChatById(existingMessage.getChatId());

        if (existingChat.getType() == ChatType.PRIVATE) {
            List<ChatMember> chatMembers = getChatMemberAdminServices.getChatMembersByChatId(existingChat.getId(), -1, 10, false);

            if (chatMembers == null || chatMembers.size() < 2) {
                throw new IllegalArgumentException("Private chat must have two members");
            }

            String userA = chatMembers.get(0).getMemberId();
            String userB = chatMembers.get(1).getMemberId();

            if (blockUserServiceApiClient.getUserBlockUser(userA, userB) != null || blockUserServiceApiClient.getUserBlockUser(userB, userA) != null) {
                throw new IllegalArgumentException("Chat members have blocked each other");
            }
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

        Chat existingChat = getChatAdminServices.getChatById(existingMessage.getChatId());

        if (existingChat.getType() == ChatType.PRIVATE) {
            List<ChatMember> chatMembers = getChatMemberAdminServices.getChatMembersByChatId(existingChat.getId(), -1, 10, false);

            if (chatMembers == null || chatMembers.size() < 2) {
                throw new IllegalArgumentException("Private chat must have two members");
            }

            String userA = chatMembers.get(0).getMemberId();
            String userB = chatMembers.get(1).getMemberId();

            if (blockUserServiceApiClient.getUserBlockUser(userA, userB) != null || blockUserServiceApiClient.getUserBlockUser(userB, userA) != null) {
                throw new IllegalArgumentException("Chat members have blocked each other");
            }
        }

        existingMessage.setStatus(false);
        existingMessage.setState(MessageState.REMOVED);
        existingMessage.setRemovedDate(Instant.now());

        messageRepository.save(existingMessage);

        AddMessageEvent event = new AddMessageEvent();
        event.setMessage(existingMessage);

        emitter.emit(event);
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

        Chat existingChat = getChatAdminServices.getChatById(fileMessageRequest.chatId);
        if (existingChat == null) {
            throw new IllegalArgumentException("Chat does not exists");
        }

        if (!existingChat.getStatus())
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

        if (existingChat.getType() == ChatType.PRIVATE) {
            List<ChatMember> chatMembers = getChatMemberAdminServices.getChatMembersByChatId(existingChat.getId(), -1, 10, false);

            if (chatMembers == null || chatMembers.size() < 2) {
                throw new IllegalArgumentException("Private chat must have two members");
            }

            String userA = chatMembers.get(0).getMemberId();
            String userB = chatMembers.get(1).getMemberId();

            if (blockUserServiceApiClient.getUserBlockUser(userA, userB) != null || blockUserServiceApiClient.getUserBlockUser(userB, userA) != null) {
                throw new IllegalArgumentException("Chat members have blocked each other");
            }
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

        AddMessageEvent event = new AddMessageEvent();
        event.setMessage(newMessage);

        emitter.emit(event);

        return newMessage;
    }

    public void deleteMessageByChatId(String chatId) {
        if (chatId == null || chatId.isBlank())
                throw new IllegalArgumentException("Chat ID cannot be null");
        
        messageRepository.deleteByChatId(chatId);
    }
    public Message addNewSystemMessage(String chatId, String content) {
        if (!MessageValidate.validateChatId(chatId)) {
            throw new IllegalArgumentException("Invalid chat id");
        }

        Chat existingChat = getChatAdminServices.getChatById(chatId);
        if (existingChat == null) {
            throw new IllegalArgumentException("Chat does not exists");
        }

        if (!existingChat.getStatus())
            throw new IllegalArgumentException("Chat have been removed");

        if (!MessageValidate.validateContent(content)) {
            throw new IllegalArgumentException("Invalid content");
        }

        Message newMessage = new Message();
        newMessage.setId(UUID.randomUUID().toString());
        newMessage.setChatId(chatId);
        newMessage.setStatus(true);
        newMessage.setState(MessageState.ORIGIN);
        newMessage.setType(MessageType.SYSTEM_MESSAGE);
        newMessage.setContent(content);
        newMessage.setSenderId("SYSTEM");
        newMessage.setSentDate(Instant.now());
        newMessage.setCreatedDate(newMessage.getSentDate());

        messageRepository.save(newMessage);

        AddMessageEvent event = new AddMessageEvent();
        event.setMessage(newMessage);

        emitter.emit(event);

        return newMessage;
    }

}
