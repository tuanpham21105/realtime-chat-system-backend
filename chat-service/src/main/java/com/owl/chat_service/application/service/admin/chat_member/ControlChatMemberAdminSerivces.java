package com.owl.chat_service.application.service.admin.chat_member;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.owl.chat_service.application.service.admin.chat.GetChatAdminServices;
import com.owl.chat_service.domain.chat.service.ChatMemberServices;
import com.owl.chat_service.domain.chat.validate.ChatMemberValidate;
import com.owl.chat_service.external_service.client.BlockUserServiceApiClient;
import com.owl.chat_service.external_service.client.UserServiceApiClient;
import com.owl.chat_service.persistence.mongodb.document.Chat;
import com.owl.chat_service.persistence.mongodb.document.ChatMember;
import com.owl.chat_service.persistence.mongodb.document.Chat.ChatType;
import com.owl.chat_service.persistence.mongodb.document.ChatMember.ChatMemberRole;
import com.owl.chat_service.persistence.mongodb.repository.ChatMemberRepository;
import com.owl.chat_service.presentation.dto.admin.ChatMemberAdminRequest;

@Service
@Transactional
public class ControlChatMemberAdminSerivces {

    private final BlockUserServiceApiClient blockUserServiceApiClient;
    private final ChatMemberRepository chatMemberRepository;
    private final GetChatMemberAdminServices getChatMemberAdminServices;
    private final GetChatAdminServices getChatAdminServices;
    private final UserServiceApiClient userServiceApiClient;

    public ControlChatMemberAdminSerivces(ChatMemberRepository chatMemberRepository, GetChatMemberAdminServices getChatMemberAdminServices, GetChatAdminServices getChatAdminServices, UserServiceApiClient userServiceApiClient, BlockUserServiceApiClient blockUserServiceApiClient) {
        this.chatMemberRepository = chatMemberRepository;
        this.getChatMemberAdminServices = getChatMemberAdminServices;
        this.getChatAdminServices = getChatAdminServices;
        this.userServiceApiClient = userServiceApiClient;
        this.blockUserServiceApiClient = blockUserServiceApiClient;}

    public ChatMember addNewChatMember(ChatMemberAdminRequest chatMemberRequest) {
        ChatMember newChatMember = new ChatMember();

        if (!ChatMemberValidate.validateMemberId(chatMemberRequest.memberId)) 
            throw new IllegalArgumentException("Invalid member id");

        if (userServiceApiClient.getUserById(chatMemberRequest.memberId) == null) 
            throw new IllegalArgumentException("Member not found");

        if (!ChatMemberValidate.validateChatId(chatMemberRequest.chatId)) 
            throw new IllegalArgumentException("Invalid chat id");

        Chat chat = getChatAdminServices.getChatById(chatMemberRequest.chatId);
        if (chat == null) 
            throw new IllegalArgumentException("Chat does not exists");

        if (!chat.getStatus())
            throw new IllegalArgumentException("Chat have been removed");

        if (chatMemberRequest.inviterId != null && !chatMemberRequest.inviterId.isBlank()) {
            if (getChatMemberAdminServices.getChatMemberByChatIdAndMemberId(chatMemberRequest.chatId, chatMemberRequest.inviterId) == null)
                throw new SecurityException("Inviter does not have permission to access this chat");
        }

        int numberOfMember = getChatMemberAdminServices.getChatMembersByChatId(chat.getId(), -1, 0, true).size();

        if (chat.getType() == ChatType.PRIVATE) {
            if (numberOfMember + 1 > 2) {
                throw new IllegalArgumentException("The chat has reached its member limit");
            }
        }
        else if (chat.getType() == ChatType.GROUP) {
            if (numberOfMember + 1 > 100) {
                throw new IllegalArgumentException("The chat has reached its member limit");
            }
        }

        if (getChatMemberAdminServices.getChatMemberByChatIdAndMemberId(chatMemberRequest.chatId, chatMemberRequest.memberId) != null) 
            throw new IllegalArgumentException("Chat member already exists");

        chatMemberRequest.role = chatMemberRequest.role.trim().toUpperCase();
        if (!ChatMemberValidate.validateRole(chatMemberRequest.role))
            throw new IllegalArgumentException("Invalid role");
        else 
            newChatMember.setRole(ChatMemberRole.valueOf(chatMemberRequest.role));

        // if (!ChatMemberValidate.validateNickname(chatMemberRequest.nickname))
        //     throw new IllegalArgumentException("Invalid nickname");

        try {
            if (blockUserServiceApiClient.getUserBlockUser(chatMemberRequest.memberId, chatMemberRequest.inviterId) != null) {
                throw new IllegalArgumentException("Inviter have been blocked by user");
            }
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        newChatMember.setId(UUID.randomUUID().toString());
        newChatMember.setMemberId(chatMemberRequest.memberId);
        newChatMember.setChatId(chatMemberRequest.chatId);
        newChatMember.setNickname(chatMemberRequest.nickname);
        newChatMember.setInviterId(chatMemberRequest.inviterId);
        newChatMember.setJoinDate(Instant.now());

        return chatMemberRepository.save(newChatMember);
    }

    public ChatMember updateChatMember(String chatId, String memberId, ChatMemberAdminRequest chatMemberRequest) {
        if (!ChatMemberValidate.validateMemberId(memberId)) 
            throw new IllegalArgumentException("Invalid member id");

        if (!ChatMemberValidate.validateChatId(chatId)) 
            throw new IllegalArgumentException("Invalid chat id");

        ChatMember existingChatMember = getChatMemberAdminServices.getChatMemberByChatIdAndMemberId(chatId, memberId);

        if (existingChatMember == null) 
            throw new IllegalArgumentException("Chat member does not exists");

        if (!ChatMemberValidate.validateMemberId(chatMemberRequest.memberId)) 
            throw new IllegalArgumentException("Invalid update member id");

        if (!ChatMemberValidate.validateChatId(chatMemberRequest.chatId)) 
            throw new IllegalArgumentException("Invalid update chat id");

        Chat chat = getChatAdminServices.getChatById(chatMemberRequest.chatId);
        if (chat == null) 
            throw new IllegalArgumentException("Chat does not exists");

        if (!chat.getStatus())
            throw new IllegalArgumentException("Chat have been removed");

        if (getChatMemberAdminServices.getChatMemberByChatIdAndMemberId(chatMemberRequest.chatId, chatMemberRequest.memberId) != null) 
            throw new IllegalArgumentException("Chat member already exists");

        chatMemberRequest.role = chatMemberRequest.role.trim().toUpperCase();
        if (!ChatMemberValidate.validateRole(chatMemberRequest.role))
            throw new IllegalArgumentException("Invalid role");
        else 
            existingChatMember.setRole(ChatMemberRole.valueOf(chatMemberRequest.role));

        existingChatMember.setMemberId(chatMemberRequest.memberId);
        existingChatMember.setChatId(chatMemberRequest.chatId);
        existingChatMember.setNickname(chatMemberRequest.nickname.trim());
        existingChatMember.setInviterId(chatMemberRequest.inviterId);
        existingChatMember.setJoinDate(Instant.now());

        return chatMemberRepository.save(existingChatMember);
    }

    public ChatMember updateChatMemberRole(String chatId, String memberId, String role) {
        if (!ChatMemberValidate.validateMemberId(memberId)) 
            throw new IllegalArgumentException("Invalid member id");

        if (!ChatMemberValidate.validateChatId(chatId)) 
            throw new IllegalArgumentException("Invalid chat id");

        Chat chat = getChatAdminServices.getChatById(chatId);
        if (chat == null) 
            throw new IllegalArgumentException("Chat does not exists");

        if (!chat.getStatus())
            throw new IllegalArgumentException("Chat have been removed");

        ChatMember existingChatMember = getChatMemberAdminServices.getChatMemberByChatIdAndMemberId(chatId, memberId);

        if (existingChatMember == null) 
            throw new IllegalArgumentException("Chat member does not exists");

        role = role.trim().toUpperCase();
        if (!ChatMemberValidate.validateRole(role))
            throw new IllegalArgumentException("Invalid role");
        else 
            existingChatMember.setRole(ChatMemberRole.valueOf(role));

        return chatMemberRepository.save(existingChatMember);
    }

    public ChatMember updateChatMemberNickname(String chatId, String memberId, String nickname) {
        if (!ChatMemberValidate.validateMemberId(memberId)) 
            throw new IllegalArgumentException("Invalid member id");

        if (!ChatMemberValidate.validateChatId(chatId)) 
            throw new IllegalArgumentException("Invalid chat id");

        ChatMember existingChatMember = getChatMemberAdminServices.getChatMemberByChatIdAndMemberId(chatId, memberId);

        if (existingChatMember == null) 
            throw new IllegalArgumentException("Chat member does not exists");

        // if (!ChatMemberValidate.validateNickname(nickname))
        //     throw new IllegalArgumentException("Invalid nickname");

        existingChatMember.setNickname(nickname.trim());

        return chatMemberRepository.save(existingChatMember);
    }

    public void deleteChatMember(String chatId, String memberId) {
        if (!ChatMemberValidate.validateMemberId(memberId)) 
            throw new IllegalArgumentException("Invalid member id");

        if (!ChatMemberValidate.validateChatId(chatId)) 
            throw new IllegalArgumentException("Invalid chat id");

        Chat chat = getChatAdminServices.getChatById(chatId);
        if (chat == null) 
            throw new IllegalArgumentException("Chat does not exists");

        if (!chat.getStatus())
            throw new IllegalArgumentException("Chat have been removed");

        ChatMember existingChatMember = getChatMemberAdminServices.getChatMemberByChatIdAndMemberId(chatId, memberId);

        if (existingChatMember == null) 
            throw new IllegalArgumentException("Chat member does not exists");

        if (ChatMemberServices.compareRole(existingChatMember.getRole(), ChatMemberRole.OWNER) == 0)
            throw new IllegalArgumentException("Chat owner cannot leave chat");

        chatMemberRepository.deleteById(Objects.requireNonNull(existingChatMember.getId(), "Delete chat member id is null"));
    }

    public void deleteChatMemberByChatId(String chatId) {
        List<ChatMember> chatMembers = getChatMemberAdminServices.getChatMembersByChatId(chatId, -1, 0, false);

        for (ChatMember chatMember : chatMembers) {
            deleteChatMember(chatId, chatMember.getMemberId());
        }
    }
}
