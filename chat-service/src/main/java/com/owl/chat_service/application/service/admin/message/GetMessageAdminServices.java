package com.owl.chat_service.application.service.admin.message;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.owl.chat_service.domain.chat.service.MessageServices;
import com.owl.chat_service.persistence.mongodb.criteria.MessageCriteria;
import com.owl.chat_service.persistence.mongodb.criteria.PagintaionCriteria;
import com.owl.chat_service.persistence.mongodb.document.Message;
import com.owl.chat_service.persistence.mongodb.repository.MessageRepository;
import com.owl.chat_service.persistence.mongodb.repository.MessageWithCriteriaRepository;
import com.owl.chat_service.presentation.dto.ResourceData;

@Service
public class GetMessageAdminServices {
    private final MessageRepository messageRepository;
    private final MessageWithCriteriaRepository messageWithCriteriaRepository;

    public GetMessageAdminServices(MessageRepository messageRepository, MessageWithCriteriaRepository messageWithCriteriaRepository) {
        this.messageRepository = messageRepository;
        this.messageWithCriteriaRepository = messageWithCriteriaRepository;
    }

    public List<Message> getMessages(Criteria criteria, int page, int size, boolean ascSort) {
        if (page == -1) {
            if (criteria == null)
                return messageWithCriteriaRepository.findAll(Sort.by(ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, "createdDate"));
            else
                return messageWithCriteriaRepository.findAll(criteria, Sort.by(ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, "createdDate"));
        }

        Pageable pageable = PagintaionCriteria.PagableCriteria(page, size, ascSort, "joinDate");

        if (criteria == null) 
            return messageWithCriteriaRepository.findAll(pageable);
        else 
            return messageWithCriteriaRepository.findAll(criteria, pageable);
    }

    public List<Message> getMessages(
        String keywords,
        int page,
        int size,
        boolean ascSort,
        Boolean status,
        String state,
        String type,
        Instant sentDateStart,
        Instant sentDateEnd,
        Instant removedDateStart,
        Instant removedDateEnd,
        Instant createdDateStart,
        Instant createdDateEnd
    ) 
    {
        Criteria criteria = MessageCriteria.FindAllMessagesWithCriteria(true, keywords, status, state, type, sentDateStart, sentDateEnd, removedDateStart, removedDateEnd, createdDateStart, createdDateEnd);

        return getMessages(criteria, page, size, ascSort);
    }

    public List<Message> getMessagesByChatId(
        String chatId,
        String keywords,
        int page,
        int size,
        boolean ascSort,
        Boolean status,
        String state,
        String type,
        Instant sentDateStart,
        Instant sentDateEnd,
        Instant removedDateStart,
        Instant removedDateEnd,
        Instant createdDateStart,
        Instant createdDateEnd
    ) 
    {
        if (chatId == null || chatId.isBlank()) 
            throw new IllegalArgumentException("Chat id cannot be null");

        Criteria criteria = MessageCriteria.FindMessagesByChatIdWithCriteria(true, chatId, keywords, status, state, type, sentDateStart, sentDateEnd, removedDateStart, removedDateEnd, createdDateStart, createdDateEnd);

        return getMessages(criteria, page, size, ascSort);
    }

    public List<Message> getMessagesBySenderId(
        String senderId,
        String keywords,
        int page,
        int size,
        boolean ascSort,
        Boolean status,
        String state,
        String type,
        Instant sentDateStart,
        Instant sentDateEnd,
        Instant removedDateStart,
        Instant removedDateEnd,
        Instant createdDateStart,
        Instant createdDateEnd
    ) 
    {
        if (senderId == null || senderId.isBlank()) 
            throw new IllegalArgumentException("Sender id cannot be null");

        Criteria criteria = MessageCriteria.FindMessagesByChatIdWithCriteria(true, senderId, keywords, status, state, type, sentDateStart, sentDateEnd, removedDateStart, removedDateEnd, createdDateStart, createdDateEnd);

        return getMessages(criteria, page, size, ascSort);
    }

    public Message getMessageById(String id) {
        if (id == null || id.isBlank()) 
            throw new IllegalArgumentException("Message id cannot be null");

        return messageRepository.findById(id).orElse(null);
    }

    public ResourceData getMessageFile(String messageId) {
        Message message = getMessageById(messageId);

        if (message == null)
            throw new IllegalArgumentException("Message not found");

        return MessageServices.loadMessageFile(message.getType(), message.getContent());
    }
}
