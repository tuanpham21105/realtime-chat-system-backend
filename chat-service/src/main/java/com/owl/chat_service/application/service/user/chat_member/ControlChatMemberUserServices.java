package com.owl.chat_service.application.service.user.chat_member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.owl.chat_service.application.service.admin.chat_member.ControlChatMemberAdminSerivces;
import com.owl.chat_service.application.service.admin.chat_member.GetChatMemberAdminServices;
import com.owl.chat_service.domain.chat.service.ChatMemberServices;
import com.owl.chat_service.domain.chat.validate.ChatMemberValidate;
import com.owl.chat_service.persistence.mongodb.document.ChatMember;
import com.owl.chat_service.persistence.mongodb.document.ChatMember.ChatMemberRole;
import com.owl.chat_service.presentation.dto.admin.ChatMemberAdminRequest;
import com.owl.chat_service.presentation.dto.user.ChatMemberCreateUserRequest;

@Service
@Transactional
public class ControlChatMemberUserServices {
    private final ControlChatMemberAdminSerivces controlChatMemberAdminSerivces;
    private final GetChatMemberAdminServices getChatMemberAdminServices;

    public ControlChatMemberUserServices(ControlChatMemberAdminSerivces controlChatMemberAdminSerivces, GetChatMemberAdminServices getChatMemberAdminServices) {
        this.controlChatMemberAdminSerivces = controlChatMemberAdminSerivces;
        this.getChatMemberAdminServices = getChatMemberAdminServices;
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

        return controlChatMemberAdminSerivces.addNewChatMember(request);
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

        return controlChatMemberAdminSerivces.updateChatMemberRole(chatId, memberId, role);
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

        return controlChatMemberAdminSerivces.updateChatMemberNickname(chatId, memberId, nickname);
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
    }
}
