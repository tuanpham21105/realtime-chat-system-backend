package com.owl.chat_service.application.service.user.chat_member;

import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import com.owl.chat_service.application.service.admin.chat.GetChatAdminServices;
import com.owl.chat_service.application.service.admin.chat_member.GetChatMemberAdminServices;
import com.owl.chat_service.domain.chat.validate.ChatMemberValidate;
import com.owl.chat_service.persistence.mongodb.criteria.ChatMemberCriteria;
import com.owl.chat_service.persistence.mongodb.document.Chat;
import com.owl.chat_service.persistence.mongodb.document.ChatMember;

@Service
public class GetChatMemberUserServices {

    private final GetChatAdminServices getChatAdminServices;
    private final GetChatMemberAdminServices getChatMemberAdminServices;

    public GetChatMemberUserServices(GetChatMemberAdminServices getChatMemberAdminServices, GetChatAdminServices getChatAdminServices) {
        this.getChatMemberAdminServices = getChatMemberAdminServices;
        this.getChatAdminServices = getChatAdminServices;}

    public List<ChatMember> getChatMembersByMemberId(
        String requesterId, 
        String keywords,
        int page,
        int size,
        boolean ascSort,
        String role,
        Instant joinDateStart,
        Instant joinDateEnd
    ) 
    {
        if (!ChatMemberValidate.validateMemberId(requesterId))
            throw new IllegalArgumentException("Invalid requester id");

        Criteria criteria = ChatMemberCriteria.FindChatMembersByMemberIdWithCriteria(false, requesterId, keywords, role, joinDateStart, joinDateEnd);

        return getChatMemberAdminServices.getChatMembers(criteria, page, size, ascSort);
    }

    public List<ChatMember> getChatMembersByChatId(
        String requesterId, 
        String chatId,
        String keywords,
        int page,
        int size,
        boolean ascSort,
        String role,
        Instant joinDateStart,
        Instant joinDateEnd
    ) 
    {
        if (!ChatMemberValidate.validateMemberId(requesterId))
            throw new IllegalArgumentException("Invalid requester id");

        if (getChatMemberAdminServices.getChatMemberByChatIdAndMemberId(chatId, requesterId) == null)
            throw new SecurityException("Requester does not have permission to access this chat members");

        Criteria criteria = ChatMemberCriteria.FindChatMembersByChatIdWithCriteria(false, chatId, keywords, role, joinDateStart, joinDateEnd);

        return getChatMemberAdminServices.getChatMembers(criteria, page, size, ascSort);
    }

    public ChatMember getChatMemberByChatIdAndMemberId(String requesterId, String memberId, String chatId) {
        if (!ChatMemberValidate.validateMemberId(requesterId))
            throw new IllegalArgumentException("Invalid requester id");

        if (!ChatMemberValidate.validateMemberId(memberId))
            throw new IllegalArgumentException("Invalid member id");

        if (!ChatMemberValidate.validateMemberId(memberId))
            throw new IllegalArgumentException("Invalid chat id");

        Chat chat = getChatAdminServices.getChatById(chatId);
        if (chat == null)
            throw new IllegalArgumentException("Chat not found");

        if (!chat.getStatus())
            throw new IllegalArgumentException("Chat have been removed");

        ChatMember chatMember = getChatMemberAdminServices.getChatMemberByChatIdAndMemberId(chatId, memberId);

        if (chatMember == null)
            throw new IllegalArgumentException("Chat member not found");

        if (getChatMemberAdminServices.getChatMemberByChatIdAndMemberId(chatId, requesterId) == null)
            throw new SecurityException("Requester does not have permission to access this member");

        return chatMember; 
    }
}
