package com.owl.chat_service.application.service.user.chat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.owl.chat_service.application.service.admin.chat.ControlChatAdminServices;
import com.owl.chat_service.application.service.admin.chat.GetChatAdminServices;
import com.owl.chat_service.application.service.admin.chat_member.ControlChatMemberAdminSerivces;
import com.owl.chat_service.application.service.admin.chat_member.GetChatMemberAdminServices;
import com.owl.chat_service.domain.chat.service.ChatMemberServices;
import com.owl.chat_service.domain.chat.validate.ChatValidate;
import com.owl.chat_service.persistence.mongodb.document.Chat;
import com.owl.chat_service.persistence.mongodb.repository.ChatRepository;
import com.owl.chat_service.persistence.mongodb.document.ChatMember;
import com.owl.chat_service.persistence.mongodb.document.ChatMember.ChatMemberRole;
import com.owl.chat_service.presentation.dto.admin.ChatAdminRequest;
import com.owl.chat_service.presentation.dto.admin.ChatMemberAdminRequest;
import com.owl.chat_service.presentation.dto.user.ChatUserRequest;

@Service
@Transactional
public class ControlChatUserServices {
    private final ControlChatAdminServices controlChatAdminServices;
    private final ControlChatMemberAdminSerivces controlChatMemberAdminSerivces;
    private final GetChatAdminServices getChatAdminServices;
    private final ChatRepository chatRepository;
    private final GetChatMemberAdminServices getChatMemberAdminServices;
    private final GetChatUserServices getChatUserServices;

    public ControlChatUserServices(ControlChatAdminServices controlChatAdminServices, ControlChatMemberAdminSerivces controlChatMemberAdminSerivces, GetChatAdminServices getChatAdminServices, ChatRepository chatRepository, GetChatMemberAdminServices getChatMemberAdminServices, GetChatUserServices getChatUserServices) {
        this.controlChatAdminServices = controlChatAdminServices;
        this.controlChatMemberAdminSerivces = controlChatMemberAdminSerivces;
        this.getChatAdminServices = getChatAdminServices;
        this.chatRepository = chatRepository;
        this.getChatMemberAdminServices = getChatMemberAdminServices;
        this.getChatUserServices = getChatUserServices;
    }

    public Chat addNewChat(String requesterId, ChatUserRequest chatRequest) {
        ChatAdminRequest request = new ChatAdminRequest();
        
        if (!chatRequest.chatMembersId.contains(requesterId)) 
            chatRequest.chatMembersId.add(requesterId);
        
        if (chatRequest.chatMembersId.size() == 0) 
            throw new IllegalArgumentException("Chat member list is empty");
        else if (chatRequest.chatMembersId.size() == 2) {
            request.type = "PRIVATE";       

            Set<Chat> member1Chats = new HashSet<>(getChatUserServices.getChatsByMemberId(chatRequest.chatMembersId.get(0), null, -1, 0, true, "PRIVATE", null, null));
            List<Chat> member2Chats = getChatUserServices.getChatsByMemberId(chatRequest.chatMembersId.get(1), null, -1, 0, true, "PRIVATE", null, null);
            
            for (Chat chat : member2Chats) {
                if (member1Chats.contains(chat)) {
                    throw new IllegalArgumentException("Private chat already exists");
                }
            }
        }
        else 
            if (chatRequest.chatMembersId.size() > 100)
                throw new IllegalArgumentException("Group chat limit is 100");
            else
                request.type = "GROUP";
        
        request.name = chatRequest.name;
        request.initiatorId = requesterId;

        Chat newChat = controlChatAdminServices.addNewChat(request);

        ChatMemberAdminRequest chatMemberRequester = new ChatMemberAdminRequest();

        chatMemberRequester.memberId = requesterId;
        chatMemberRequester.chatId = newChat.getId();
        chatMemberRequester.role = "OWNER";
        chatMemberRequester.inviterId = null;
        controlChatMemberAdminSerivces.addNewChatMember(chatMemberRequester);

        for (String memberId : chatRequest.chatMembersId) {
            ChatMemberAdminRequest chatMemberRequest = new ChatMemberAdminRequest();
            chatMemberRequest.memberId = memberId;
            chatMemberRequest.chatId = newChat.getId();
            chatMemberRequest.inviterId = requesterId;
            if (memberId.compareToIgnoreCase(requesterId) == 0) {
                continue;
            }
            else if (request.type == "PRIVATE")
                chatMemberRequest.role = "OWNER";
            else 
                chatMemberRequest.role = "MEMBER";

            controlChatMemberAdminSerivces.addNewChatMember(chatMemberRequest);
        }

        return newChat;
    }

    public Chat updateChatName(String requesterId, String chatId, String name) {
        ChatMember chatMember = getChatMemberAdminServices.getChatMemberByChatIdAndMemberId(chatId, requesterId);

        if (chatMember == null)
            throw new SecurityException("Requester does not have permission to access this chat");

        if (ChatMemberServices.compareRole(chatMember.getRole(), ChatMemberRole.ADMIN) < 0) 
            throw new SecurityException("Requester does not have permission to rename this chat");

        if (!ChatValidate.validateName(name)) 
            throw new IllegalArgumentException("Invalid name");

        Chat chat = getChatAdminServices.getChatById(chatId);

        if (!chat.getStatus())
            throw new IllegalArgumentException("Chat have been removed");

        chat.setName(name);

        return chatRepository.save(chat);
    }

    public void deleteChat(String requesterId, String chatId) {
        ChatMember chatMember = getChatMemberAdminServices.getChatMemberByChatIdAndMemberId(chatId, requesterId);

        if (chatMember == null)
            throw new SecurityException("Requester does not have permission to delete this chat");

        if (ChatMemberServices.compareRole(chatMember.getRole(), ChatMemberRole.OWNER) < 0) 
            throw new SecurityException("Requester does not have permission to delete this chat");

        controlChatAdminServices.softDeleteChat(chatId);
    }
    
    public void updateChatAvatarFile(String requesterId, String chatId, MultipartFile file) {
        ChatMember chatMember = getChatMemberAdminServices.getChatMemberByChatIdAndMemberId(chatId, requesterId);

        if (chatMember == null)
            throw new SecurityException("Requester does not have permission to access this chat");

        if (ChatMemberServices.compareRole(chatMember.getRole(), ChatMemberRole.ADMIN) < 0)
            throw new SecurityException("Requester does not have permission to set this chat avatar");

        controlChatAdminServices.updateChatAvatarFile(chatId, file);
    }
}
