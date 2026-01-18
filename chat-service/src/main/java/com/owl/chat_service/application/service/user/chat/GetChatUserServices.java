package com.owl.chat_service.application.service.user.chat;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import com.owl.chat_service.application.service.admin.chat.GetChatAdminServices;
import com.owl.chat_service.application.service.admin.chat_member.GetChatMemberAdminServices;
import com.owl.chat_service.domain.chat.validate.ChatMemberValidate;
import com.owl.chat_service.domain.chat.validate.ChatValidate;
import com.owl.chat_service.persistence.mongodb.criteria.ChatCriteria;
import com.owl.chat_service.persistence.mongodb.criteria.PagintaionCriteria;
import com.owl.chat_service.persistence.mongodb.document.Chat;
import com.owl.chat_service.persistence.mongodb.document.ChatMember;
import com.owl.chat_service.persistence.mongodb.repository.ChatRepository;
import com.owl.chat_service.persistence.mongodb.repository.ChatWithCriteriaRepository;
import com.owl.chat_service.presentation.dto.ResourceData;

@Service
public class GetChatUserServices {

    private final GetChatAdminServices getChatAdminServices;
    private final GetChatMemberAdminServices getChatMemberAdminServices;
    private final ChatRepository chatRepository;
    private final ChatWithCriteriaRepository chatWithCriteriaRepository;

    public GetChatUserServices(GetChatMemberAdminServices getChatMemberAdminServices, ChatRepository chatRepository, ChatWithCriteriaRepository chatWithCriteriaRepository, GetChatAdminServices getChatAdminServices) {
        this.getChatMemberAdminServices = getChatMemberAdminServices;
        this.chatRepository = chatRepository;
        this.chatWithCriteriaRepository = chatWithCriteriaRepository;
        this.getChatAdminServices = getChatAdminServices;}

    public List<Chat> getChatsByMemberId(
        String requesterId, 
        String memberId,
        String keywords,
        int page,
        int size,
        boolean ascSort,
        String type,
        Instant joinDateStart,
        Instant joinDateEnd
    ) 
    {
        if (!ChatMemberValidate.validateMemberId(requesterId))
            throw new IllegalArgumentException("Invalid requester id");

        if (!ChatMemberValidate.validateMemberId(memberId))
            throw new IllegalArgumentException("Invalid member id");

        if (!ChatMemberValidate.validateRequesterAndMemberAreSame(requesterId, memberId))
            throw new SecurityException("Requester does not have permission to access this member chats");

        List<ChatMember> chatMembers = getChatMemberAdminServices.getChatMembersByMemberId(memberId, null, -1, 1, true, null, joinDateStart, joinDateEnd);

        List<String> chatsId = new ArrayList<>();
        for (ChatMember chatMember : chatMembers) {
            chatsId.add(chatMember.getChatId());
        }

        Criteria criteria = ChatCriteria.FindChatsInIdsListWithCriteria(chatsId, keywords, true, type, null, null, null);

        if (page == -1) {
            if (criteria == null) 
                return chatWithCriteriaRepository.findAll(Sort.by(ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, "newestMessageDate"));
            else 
                return chatWithCriteriaRepository.findAll(criteria, Sort.by(ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, "newestMessageDate"));
        }

        Pageable pageable = PagintaionCriteria.PagableCriteria(page, size, ascSort, "newestMessageDate");
        
        if (criteria == null) 
            return chatWithCriteriaRepository.findAll(pageable);
        else 
            return chatWithCriteriaRepository.findAll(criteria, pageable);
    }

    public Chat getChatById(String requesterId, String chatId) {
        if (!ChatMemberValidate.validateMemberId(requesterId))
            throw new IllegalArgumentException("Invalid requester id");

        if (!ChatValidate.validateChatId(chatId))
            throw new IllegalArgumentException("Invalid chat id");

        if (getChatMemberAdminServices.getChatMemberByChatIdAndMemberId(chatId, requesterId) == null) 
            throw new SecurityException("Requester does not have permission to access this chat");

        return chatRepository.findById(Objects.requireNonNull(chatId, "Chat id is null")).orElse(null);
    }

    public ResourceData getChatAvatar(String requesterId, String chatId) {
        Chat chat = getChatById(requesterId, chatId);

        if (chat == null) 
            throw new IllegalArgumentException("Chat not found");

        return getChatAdminServices.getChatAvatarFile(chatId);
    }
}
