package com.owl.chat_service.application.service.admin.chat_member;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.owl.chat_service.persistence.mongodb.criteria.ChatMemberCriteria;
import com.owl.chat_service.persistence.mongodb.criteria.PagintaionCriteria;
import com.owl.chat_service.persistence.mongodb.document.ChatMember;
import com.owl.chat_service.persistence.mongodb.repository.ChatMemberRepository;
import com.owl.chat_service.persistence.mongodb.repository.ChatMemberWithCriteriaRepository;

@Service
public class GetChatMemberAdminServices {
    private final ChatMemberRepository chatMemberRepository;
    private final ChatMemberWithCriteriaRepository chatMemberWithCriteriaRepository;

    public GetChatMemberAdminServices(ChatMemberRepository chatMemberRepository, ChatMemberWithCriteriaRepository chatMemberWithCriteriaRepository) {
        this.chatMemberRepository = chatMemberRepository;
        this.chatMemberWithCriteriaRepository = chatMemberWithCriteriaRepository;
    }

    public List<ChatMember> getChatMembers(Criteria criteria, int page, int size, boolean ascSort) {
        if (page == -1) {
            if (criteria == null)
                return chatMemberWithCriteriaRepository.findAll(Sort.by(ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, "joinDate"));
            else
                return chatMemberWithCriteriaRepository.findAll(criteria, Sort.by(ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, "joinDate"));
        }

        Pageable pageable = PagintaionCriteria.PagableCriteria(page, size, ascSort, "joinDate");

        if (criteria == null)
            return chatMemberWithCriteriaRepository.findAll(pageable);
        else 
            return chatMemberWithCriteriaRepository.findAll(criteria, pageable);
    }

    public List<ChatMember> getChatMembers(
        String keywords,
        int page,
        int size,
        boolean ascSort,
        String role,
        Instant joinDateStart,
        Instant joinDateEnd
    ) 
    {
        Criteria criteria = ChatMemberCriteria.FindAllChatMembersWithCriteria(true, keywords, role, joinDateStart, joinDateEnd);

        return getChatMembers(criteria, page, size, ascSort);
    }

    public List<ChatMember> getChatMembersByChatId(
        String chatId,
        int page,
        int size,
        boolean ascSort
    ) 
    {
        if (chatId == null || chatId.isBlank()) 
            throw new IllegalArgumentException("Chat id cannot be null"); 

        Criteria criteria = ChatMemberCriteria.FindChatMembersByChatIdWithCriteria(true, chatId, null, null, null, null);

        if (page == -1) {
            return chatMemberWithCriteriaRepository.findAll(criteria, Sort.by(ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, "joinDate"));
        }

        Pageable pageable = PagintaionCriteria.PagableCriteria(page, size, ascSort, "joinDate");

        return chatMemberWithCriteriaRepository.findAll(criteria, pageable);
    }

    public List<ChatMember> getChatMembersByChatId(
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
        if (chatId == null || chatId.isBlank()) 
            throw new IllegalArgumentException("Chat id cannot be null"); 

        Criteria criteria = ChatMemberCriteria.FindChatMembersByChatIdWithCriteria(true, chatId, keywords, role, joinDateStart, joinDateEnd);

        return getChatMembers(criteria, page, size, ascSort);
    }

    public List<ChatMember> getChatMembersByMemberId(
        String memberId,
        String keywords,
        int page,
        int size,
        boolean ascSort,
        String role,
        Instant joinDateStart,
        Instant joinDateEnd
    ) 
    {
        if (memberId == null || memberId.isBlank()) 
            throw new IllegalArgumentException("Member id cannot be null"); 

        Criteria criteria = ChatMemberCriteria.FindChatMembersByMemberIdWithCriteria(true, memberId, keywords, role, joinDateStart, joinDateEnd);

        return getChatMembers(criteria, page, size, ascSort);
    }

    public ChatMember getChatMemberByChatIdAndMemberId(String chatId, String memberId) {
        if (chatId == null || chatId.isBlank()) 
            throw new IllegalArgumentException("Chat id cannot be null"); 
        
        if (memberId == null || memberId.isBlank()) 
            throw new IllegalArgumentException("Member id cannot be null"); 

        return chatMemberRepository.findByChatIdAndMemberId(chatId, memberId).orElse(null);
    }
}
