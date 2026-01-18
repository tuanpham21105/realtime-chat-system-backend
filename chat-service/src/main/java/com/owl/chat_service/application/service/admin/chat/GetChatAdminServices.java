package com.owl.chat_service.application.service.admin.chat;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.owl.chat_service.domain.chat.service.ChatServices;
import com.owl.chat_service.persistence.mongodb.criteria.ChatCriteria;
import com.owl.chat_service.persistence.mongodb.criteria.PagintaionCriteria;
import com.owl.chat_service.persistence.mongodb.document.Chat;
import com.owl.chat_service.persistence.mongodb.repository.ChatRepository;
import com.owl.chat_service.persistence.mongodb.repository.ChatWithCriteriaRepository;
import com.owl.chat_service.presentation.dto.ResourceData;

@Service
public class GetChatAdminServices {
    private final ChatRepository chatRepository;
    private final ChatWithCriteriaRepository chatWithCriteriaRepository;

    public GetChatAdminServices(ChatRepository chatRepository, ChatWithCriteriaRepository chatWithCriteriaRepository) {
        this.chatRepository = chatRepository;
        this.chatWithCriteriaRepository = chatWithCriteriaRepository;
    }

    public List<Chat> getChats
    (
        String keywords,
        int page,
        int size,
        boolean ascSort,
        Boolean status,
        String type,
        String initiatorId,
        Instant createdDateStart,
        Instant createdDateEnd
    ) 
    {
        Criteria criteria = ChatCriteria.FindAllChatsWithCriteria(keywords, status, type, initiatorId, createdDateStart, createdDateEnd);

        if (page == -1) {
            if (criteria == null)
                return chatWithCriteriaRepository.findAll(Sort.by(ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, "createdDate"));
            else
            return chatWithCriteriaRepository.findAll(criteria, Sort.by(ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, "createdDate"));
        }

        Pageable pageable = PagintaionCriteria.PagableCriteria(page, size, ascSort, "joinDate");
        
        
        if (criteria == null)
            return chatWithCriteriaRepository.findAll(pageable);
        else
            return chatWithCriteriaRepository.findAll(criteria, pageable);
    }

    public Chat getChatById(String chatId) {
        if (chatId == null || chatId.isBlank()) {
            throw new IllegalArgumentException("Chat id cannot be null");
        }

        return chatRepository.findById(chatId).orElse(null);
    }
    public ResourceData getChatAvatarFile(String chatId) {
        String avatar = getChatById(chatId).getAvatar();

        return ChatServices.loadAvatar(avatar);
    }    
}
