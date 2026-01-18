package com.owl.chat_service.application.service.user.message;

import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import com.owl.chat_service.application.service.admin.chat_member.GetChatMemberAdminServices;
import com.owl.chat_service.application.service.admin.message.GetMessageAdminServices;
import com.owl.chat_service.application.service.user.chat.GetChatUserServices;
import com.owl.chat_service.domain.chat.validate.ChatMemberValidate;
import com.owl.chat_service.persistence.mongodb.criteria.MessageCriteria;
import com.owl.chat_service.persistence.mongodb.document.Chat;
import com.owl.chat_service.persistence.mongodb.document.Message;
import com.owl.chat_service.persistence.mongodb.document.Message.MessageState;
import com.owl.chat_service.presentation.dto.ResourceData;

@Service
public class GetMessageUserServices {

    private final GetChatMemberAdminServices getChatMemberAdminServices;
    private final GetChatUserServices getChatUserServices;
    private final GetMessageAdminServices getMessageAdminServices;

    public GetMessageUserServices(GetChatUserServices getChatUserServices, GetMessageAdminServices getMessageAdminServices, GetChatMemberAdminServices getChatMemberAdminServices) {
        this.getChatUserServices = getChatUserServices;
        this.getMessageAdminServices = getMessageAdminServices;
        this.getChatMemberAdminServices = getChatMemberAdminServices;
    }

    public List<Message> getMessagesByChatId(
        String requesterId,
        String chatId,
        String keywords,
        int page,
        int size,
        boolean ascSort,
        String type,
        String senderId,
        Instant sentDateStart,
        Instant sentDateEnd 
    ) 
    {
        Chat chat = getChatUserServices.getChatById(requesterId, chatId);

        if (chat == null) 
            throw new IllegalArgumentException("Chat not found");

        Criteria criteria = MessageCriteria.FindMessagesByChatIdAndSenderIdWithCriteria(true, chatId, senderId, keywords, null, null, type, sentDateStart, sentDateEnd, null, null, null, null);

        List<Message> messages = getMessageAdminServices.getMessages(criteria, page, size, ascSort);

        messages.forEach(m -> {
            if (m.getState() == MessageState.REMOVED) {
                m.setContent(null);
            }
        });

        return messages;
    }

    public Message getMessageById(String requesterId, String messageId) {
        if (!ChatMemberValidate.validateMemberId(requesterId)) 
            throw new IllegalArgumentException("Invalid requester id");

        Message message = getMessageAdminServices.getMessageById(messageId);

        if (message == null)
            throw new IllegalArgumentException("Message not found");
        
        if (getChatMemberAdminServices.getChatMemberByChatIdAndMemberId(message.getChatId(), requesterId) == null)
            throw new SecurityException("Requester does not have permission to access this message");

        return message;
    }

    public ResourceData getMessageFile(String requesterId, String messageId) {
        Message message = getMessageById(requesterId, messageId);

        if (message == null) 
            throw new IllegalArgumentException("Message not found");

        return getMessageAdminServices.getMessageFile(messageId);
    }
}
