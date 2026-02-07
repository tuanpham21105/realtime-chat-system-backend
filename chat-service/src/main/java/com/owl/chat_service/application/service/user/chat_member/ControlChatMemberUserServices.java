package com.owl.chat_service.application.service.user.chat_member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.owl.chat_service.application.service.admin.chat_member.ControlChatMemberAdminSerivces;
import com.owl.chat_service.application.service.admin.chat_member.GetChatMemberAdminServices;
import com.owl.chat_service.application.service.event.EventEmitter;
import com.owl.chat_service.application.service.event.SystemMessageEvent;
import com.owl.chat_service.domain.chat.service.ChatMemberServices;
import com.owl.chat_service.domain.chat.validate.ChatMemberValidate;
import com.owl.chat_service.external_service.client.UserServiceApiClient;
import com.owl.chat_service.external_service.dto.UserProfileDto;
import com.owl.chat_service.persistence.mongodb.document.ChatMember;
import com.owl.chat_service.persistence.mongodb.document.ChatMember.ChatMemberRole;
import com.owl.chat_service.presentation.dto.admin.ChatMemberAdminRequest;
import com.owl.chat_service.presentation.dto.user.ChatMemberCreateUserRequest;

@Service
@Transactional
public class ControlChatMemberUserServices {
    private final ControlChatMemberAdminSerivces controlChatMemberAdminSerivces;
    private final GetChatMemberAdminServices getChatMemberAdminServices;
    private final EventEmitter eventEmitter;
    private final UserServiceApiClient userServiceApiClient;

    public ControlChatMemberUserServices(ControlChatMemberAdminSerivces controlChatMemberAdminSerivces, GetChatMemberAdminServices getChatMemberAdminServices, EventEmitter eventEmitter, UserServiceApiClient userServiceApiClient) {
        this.controlChatMemberAdminSerivces = controlChatMemberAdminSerivces;
        this.getChatMemberAdminServices = getChatMemberAdminServices;
        this.eventEmitter = eventEmitter;
        this.userServiceApiClient = userServiceApiClient;
    }

    public ChatMember addNewChatMember(String requesterId, ChatMemberCreateUserRequest chatMemberCreateRequest) {
        ChatMember chatMember = getChatMemberAdminServices.getChatMemberByChatIdAndMemberId(chatMemberCreateRequest.chatId, requesterId);

        if (chatMember == null) 
            throw new SecurityException("Requester does not have permission to add member to this chat");

        ChatMemberAdminRequest request = new ChatMemberAdminRequest();
        request.chatId = chatMemberCreateRequest.chatId;
        request.inviterId = requesterId;
        request.memberId = chatMemberCreateRequest.memberId;
        request.role = "USER";

        ChatMember newChatMember = controlChatMemberAdminSerivces.addNewChatMember(request);

        UserProfileDto requester = userServiceApiClient.getUserById(requesterId);
        UserProfileDto member = userServiceApiClient.getUserById(request.memberId);

        SystemMessageEvent event = new SystemMessageEvent();
        event.setChatId(request.chatId);
        event.setContent(member.name + " has been added to the chat by " + requester.name);
        eventEmitter.emit(event);

        return newChatMember;
    }

    public ChatMember updateChatMemberRole(String requesterId, String memberId, String chatId, String role) {
        ChatMember requesterChatMember = getChatMemberAdminServices.getChatMemberByChatIdAndMemberId(chatId, requesterId);
        ChatMember chatMember = getChatMemberAdminServices.getChatMemberByChatIdAndMemberId(chatId, memberId);

        if (requesterChatMember == null) 
            throw new SecurityException("Requester does not have permission to access this chat");

        if (chatMember == null) 
            throw new IllegalArgumentException("Chat member not found");

        role = role.trim().toUpperCase();
        if (!ChatMemberValidate.validateRole(role))
            throw new IllegalArgumentException("Invalid role");

        if (ChatMemberServices.compareRole(requesterChatMember.getRole(), ChatMemberRole.ADMIN) < 0)
            throw new SecurityException("Requester does not have permission to set member role");

        if (ChatMemberServices.compareRole(requesterChatMember.getRole(), chatMember.getRole()) <= 0) 
            throw new SecurityException("Member have role higher or equal to requester");

        if (ChatMemberServices.compareRole(requesterChatMember.getRole(), ChatMemberRole.valueOf(role)) < 0) 
            throw new SecurityException("Requester does not have permission to set this member higher role");

        if (ChatMemberRole.valueOf(role) == ChatMemberRole.OWNER) 
            controlChatMemberAdminSerivces.updateChatMemberRole(chatId, requesterId, "MEMBER");

        ChatMember updateChatMember = controlChatMemberAdminSerivces.updateChatMemberRole(chatId, memberId, role);

        UserProfileDto requester = userServiceApiClient.getUserById(requesterId);
        UserProfileDto member = userServiceApiClient.getUserById(memberId);

        SystemMessageEvent event = new SystemMessageEvent();
        event.setChatId(chatId);
        event.setContent(member.name + "\'role has been updated to " + role + " by " + requester.name);
        eventEmitter.emit(event);

        return updateChatMember;
    }

    public ChatMember updateChatMemberNickname(String requesterId, String memberId, String chatId, String nickname) {
        ChatMember requesterChatMember = getChatMemberAdminServices.getChatMemberByChatIdAndMemberId(chatId, requesterId);
        ChatMember chatMember = getChatMemberAdminServices.getChatMemberByChatIdAndMemberId(chatId, memberId);

        if (requesterChatMember == null) 
            throw new SecurityException("Requester does not have permission to access this chat");

        if (chatMember == null) 
            throw new IllegalArgumentException("Chat member not found");

        if (ChatMemberServices.compareRole(requesterChatMember.getRole(), ChatMemberRole.MEMBER) < 0)
            throw new SecurityException("Requester does not have permission to set member nickname");

        ChatMember updateChatMember = controlChatMemberAdminSerivces.updateChatMemberNickname(chatId, memberId, nickname);

        UserProfileDto requester = userServiceApiClient.getUserById(requesterId);
        UserProfileDto member = userServiceApiClient.getUserById(memberId);

        SystemMessageEvent event = new SystemMessageEvent();
        event.setChatId(chatId);
        event.setContent(member.name + "\'nickname has been updated to " + nickname + " by " + requester.name);
        eventEmitter.emit(event);

        return updateChatMember;
    }

    public void deleteChatMember(String requesterId, String memberId, String chatId) {
        ChatMember requesterChatMember = getChatMemberAdminServices.getChatMemberByChatIdAndMemberId(chatId, requesterId);
        ChatMember chatMember = getChatMemberAdminServices.getChatMemberByChatIdAndMemberId(chatId, memberId);

        if (requesterChatMember == null) 
            throw new SecurityException("Requester does not have permission to access this chat");

        if (chatMember == null) 
            throw new IllegalArgumentException("Chat member not found");

        if (!ChatMemberValidate.validateRequesterAndMemberAreSame(requesterId, memberId)){
            if (ChatMemberServices.compareRole(requesterChatMember.getRole(), ChatMemberRole.ADMIN) < 0)
                throw new SecurityException("Requester does not have permission to remove member");

            if (ChatMemberServices.compareRole(requesterChatMember.getRole(), chatMember.getRole()) <= 0) 
                throw new SecurityException("Member have role higher or equal to requester");
        }

        controlChatMemberAdminSerivces.deleteChatMember(chatId, memberId);
        
        UserProfileDto requester = userServiceApiClient.getUserById(requesterId);
        UserProfileDto member = userServiceApiClient.getUserById(memberId);

        SystemMessageEvent event = new SystemMessageEvent();
        event.setChatId(chatId);
        event.setContent(member.name + " has been removed from the chat by " + requester.name);
        eventEmitter.emit(event);

    }
}
