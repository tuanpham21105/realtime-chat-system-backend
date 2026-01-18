package com.owl.chat_service.persistence.mongodb.criteria;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.mongodb.core.query.Criteria;

import com.owl.chat_service.domain.chat.validate.MessageValidate;
import com.owl.chat_service.infrastructure.utils.KeywordUtils;
import com.owl.chat_service.persistence.mongodb.document.Message.MessageType;

public class MessageCriteria {
    public static Criteria FindAllMessagesWithCriteria(
        boolean idSearch,
        String keywords, 
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
        List<Criteria> criteriaList = new ArrayList<>();

        // keyword search (name)
        if (keywords != null && !keywords.isBlank() && MessageType.valueOf(type.toUpperCase()).equals(MessageType.TEXT)) {
            List<Criteria> keywordsCriteriaList = new ArrayList<Criteria>();
            for (String keyword : KeywordUtils.parseKeywords(keywords)) {    
                if (keyword == null) continue;
                keywordsCriteriaList.add(
                    Criteria.where("content")
                            .regex(keyword, "i")
                );

                if (idSearch) {
                    keywordsCriteriaList.add(
                        Criteria.where("id")
                                .regex(keyword, "i")
                    );
                    keywordsCriteriaList.add(
                        Criteria.where("chatId")
                                .regex(keyword, "i")
                    );
                    keywordsCriteriaList.add(
                        Criteria.where("senderId")
                                .regex(keyword, "i")
                    );
                    keywordsCriteriaList.add(
                        Criteria.where("predecessorId")
                                .regex(keyword, "i")
                    );
                }
            }
            criteriaList.add(new Criteria().orOperator(keywordsCriteriaList));
        }

        // status
        if (status != null) {
            criteriaList.add(
                Criteria.where("status").is(status)
            );
        }

        // state
        if (state != null && !state.isBlank() && MessageValidate.ValidateState(state)) {
            criteriaList.add(Criteria.where("state").is(state));
        }

        // type
        if (type != null && !type.isBlank() && MessageValidate.ValidateType(type)) {
            criteriaList.add(Criteria.where("type").is(type));
        }

        // created date range
        if (createdDateStart != null || createdDateEnd != null) {
            Criteria dateCriteria = Criteria.where("createdDate");

            if (createdDateStart != null) {
                dateCriteria.gte(Objects.requireNonNull(createdDateStart));
            }
            if (createdDateEnd != null) {
                dateCriteria.lte(Objects.requireNonNull(createdDateEnd));
            }

            criteriaList.add(dateCriteria);
        }

        // sent date range
        if (sentDateStart != null || sentDateEnd != null) {
            Criteria dateCriteria = Criteria.where("sentDate");

            if (sentDateStart != null) {
                dateCriteria.gte(Objects.requireNonNull(sentDateStart));
            }
            if (sentDateEnd != null) {
                dateCriteria.lte(Objects.requireNonNull(sentDateEnd));
            }

            criteriaList.add(dateCriteria);
        }

        // removed date range
        if (removedDateStart != null || removedDateEnd != null) {
            Criteria dateCriteria = Criteria.where("removedDate");

            if (removedDateStart != null) {
                dateCriteria.gte(Objects.requireNonNull(removedDateStart));
            }
            if (removedDateEnd != null) {
                dateCriteria.lte(Objects.requireNonNull(removedDateEnd));
            }

            criteriaList.add(dateCriteria);
        }

        return criteriaList.isEmpty() ? null : new Criteria().andOperator(criteriaList);
    }

    public static Criteria FindMessagesByChatIdWithCriteria(
        boolean idSearch,
        String chatId,
        String keywords, 
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
        List<Criteria> criteriaList = new ArrayList<>();

        //chat id
        if (chatId != null && !chatId.isEmpty()) {
            criteriaList.add(Criteria.where("chatId").is(chatId));
        }

        criteriaList.add(FindAllMessagesWithCriteria(idSearch, keywords, status, state, type, sentDateStart, sentDateEnd, removedDateStart, removedDateEnd, createdDateStart, createdDateEnd));

        return criteriaList.isEmpty() ? null : new Criteria().andOperator(criteriaList);
    }

    public static Criteria FindMessagesBySenderIdWithCriteria(
        boolean idSearch,
        String senderId,
        String keywords, 
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
        List<Criteria> criteriaList = new ArrayList<>();

        // sender id
        if (senderId != null && !senderId.isEmpty()) {
            criteriaList.add(Criteria.where("senderId").is(senderId));
        }

        Criteria criteria = FindAllMessagesWithCriteria(idSearch, keywords, status, state, type, sentDateStart, sentDateEnd, removedDateStart, removedDateEnd, createdDateStart, createdDateEnd);
        if (criteria != null)
            criteriaList.add(criteria);

        return criteriaList.isEmpty() ? null : new Criteria().andOperator(criteriaList);
    }

    public static Criteria FindMessagesByChatIdAndSenderIdWithCriteria(
        boolean idSearch,
        String chatId,
        String senderId,
        String keywords, 
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
        List<Criteria> criteriaList = new ArrayList<>();

        //chat id
        if (chatId != null && !chatId.isEmpty()) {
            criteriaList.add(Criteria.where("chatId").is(chatId));
        }

        // sender id
        if (senderId != null && !senderId.isEmpty()) {
            criteriaList.add(Criteria.where("senderId").is(senderId));
        }

        Criteria criteria = FindAllMessagesWithCriteria(idSearch, keywords, status, state, type, sentDateStart, sentDateEnd, removedDateStart, removedDateEnd, createdDateStart, createdDateEnd);
        if (criteria != null)
            criteriaList.add(criteria);

        return criteriaList.isEmpty() ? null : new Criteria().andOperator(criteriaList);
    }
}
