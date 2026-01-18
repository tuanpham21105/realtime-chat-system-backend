package com.owl.chat_service.persistence.mongodb.criteria;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.mongodb.core.query.Criteria;

import com.owl.chat_service.infrastructure.utils.KeywordUtils;
import com.owl.chat_service.persistence.mongodb.document.Chat.ChatType;

public class ChatCriteria {

    public static Criteria FindAllChatsWithCriteria(
        String keywords,
        Boolean status,
        String type,
        String initiatorId,
        Instant createdDateStart,
        Instant createdDateEnd
    ) 
    {
        List<Criteria> criteriaList = new ArrayList<>();

        // keyword search (name)
        if (keywords != null && !keywords.isBlank()) {
            List<Criteria> keywordsCriteriaList = new ArrayList<Criteria>();
            for (String keyword : KeywordUtils.parseKeywords(keywords)) {    
                if (keyword == null) continue;
                keywordsCriteriaList.add(
                    Criteria.where("name")
                            .regex(keyword, "i")
                );
            }
            criteriaList.add(new Criteria().orOperator(keywordsCriteriaList));
        }

        // status
        if (status != null) {
            if (status) {
                criteriaList.add(
                    Criteria.where("status").is(true)
                );
            }
            else if (!status) {
                criteriaList.add(
                    Criteria.where("status").is(false)
                );
            }
        }

        // type
        if (type != null && !type.isBlank()) {
            criteriaList.add(
                Criteria.where("type").is(ChatType.valueOf(type))
            );
        }

        // initiator
        if (initiatorId != null && !initiatorId.isBlank()) {
            criteriaList.add(
                Criteria.where("initiatorId").is(initiatorId)
            );
        }

        // created date range
        if (createdDateStart != null || createdDateEnd != null) {
            Criteria dateCriteria = Criteria.where("createdDate");

            if (createdDateStart != null) {
                dateCriteria.gte(Objects.requireNonNull(createdDateStart, "createdDateStart cannot be null"));
            }
            if (createdDateEnd != null) {
                dateCriteria.gte(Objects.requireNonNull(createdDateEnd, "createdDateEnd cannot be null"));
            }

            criteriaList.add(dateCriteria);
        }

        return criteriaList.isEmpty() ? null : new Criteria().andOperator(criteriaList);
    }

    public static Criteria FindChatsInIdsListWithCriteria(
        List<String> idsList,
        String keywords,
        Boolean status,
        String type,
        String initiatorId,
        Instant createdDateStart,
        Instant createdDateEnd
    ) 
    {
        List<Criteria> criteriaList = new ArrayList<>();

        // id in list
        if (!idsList.isEmpty())
            criteriaList.add(Criteria.where("_id").in(idsList));

        // keyword search (name)
        if (keywords != null && !keywords.isBlank()) {
            List<Criteria> keywordsCriteriaList = new ArrayList<Criteria>();
            for (String keyword : KeywordUtils.parseKeywords(keywords)) {    
                if (keyword == null) continue;
                keywordsCriteriaList.add(
                    Criteria.where("name")
                            .regex(keyword, "i")
                );
            }
            criteriaList.add(new Criteria().orOperator(keywordsCriteriaList));
        }

        // status
        if (status != null) {
            criteriaList.add(
                Criteria.where("status").is(status)
            );
        }

        // type
        if (type != null && !type.isBlank()) {
            criteriaList.add(
                Criteria.where("type").is(ChatType.valueOf(type))
            );
        }

        // initiator
        if (initiatorId != null && !initiatorId.isBlank()) {
            criteriaList.add(
                Criteria.where("initiatorId").is(initiatorId)
            );
        }

        // created date range
        if (createdDateStart != null || createdDateEnd != null) {
            Criteria dateCriteria = Criteria.where("createdDate");

            if (createdDateStart != null) {
                dateCriteria.gte(Objects.requireNonNull(createdDateStart, "createdDateStart cannot be null"));
            }
            if (createdDateEnd != null) {
                dateCriteria.gte(Objects.requireNonNull(createdDateEnd, "createdDateEnd cannot be null"));
            }

            criteriaList.add(dateCriteria);
        }

        return criteriaList.isEmpty() ? null : new Criteria().andOperator(criteriaList);
    }
}
