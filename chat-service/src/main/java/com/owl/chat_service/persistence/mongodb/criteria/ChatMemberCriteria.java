package com.owl.chat_service.persistence.mongodb.criteria;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.mongodb.core.query.Criteria;

import com.owl.chat_service.domain.chat.validate.ChatMemberValidate;
import com.owl.chat_service.infrastructure.utils.KeywordUtils;

public class ChatMemberCriteria {
    public static Criteria FindAllChatMembersWithCriteria(
        boolean idSearch,
        String keywords, 
        String role,
        Instant joindDateStart,
        Instant joinDateEnd
    ) 
    {
        List<Criteria> criteriaList = new ArrayList<>();

        // keyword search (name)
        if (keywords != null && !keywords.isBlank()) {
            List<Criteria> keywordsCriteriaList = new ArrayList<Criteria>();
            for (String keyword : KeywordUtils.parseKeywords(keywords)) {    
                if (keyword == null) continue;
                keywordsCriteriaList.add(
                    Criteria.where("nickname")
                            .regex(keyword, "i")
                );
                
                if (idSearch) {
                    keywordsCriteriaList.add(
                        Criteria.where("memberId")
                                .regex(keyword, "i")
                    );
                    keywordsCriteriaList.add(
                        Criteria.where("chatId")
                                .regex(keyword, "i")
                    );
                    keywordsCriteriaList.add(
                        Criteria.where("inviterId")
                                .regex(keyword, "i")
                    );
                }
            }
            criteriaList.add(new Criteria().orOperator(keywordsCriteriaList));
        }

        // role
        if (role != null && !role.isBlank() && ChatMemberValidate.validateRole(role)) {
            criteriaList.add(Criteria.where("role").is(role));
        }

        // join date range
        if (joindDateStart != null || joinDateEnd != null) {
            Criteria dateCriteria = Criteria.where("joinDate");

            if (joindDateStart != null) {
                dateCriteria.gte(Objects.requireNonNull(joindDateStart, "joindDateStart cannot be null"));
            }
            if (joinDateEnd != null) {
                dateCriteria.lte(Objects.requireNonNull(joinDateEnd, "joinDateEnd cannot be null"));
            }

            criteriaList.add(dateCriteria);
        }

        return criteriaList.isEmpty() ? null : new Criteria().andOperator(criteriaList);
    }  
    
    public static Criteria FindChatMembersByMemberIdWithCriteria(
        boolean idSearch,
        String memberId,
        String keywords, 
        String role,
        Instant joindDateStart,
        Instant joinDateEnd
    ) 
    {
        List<Criteria> criteriaList = new ArrayList<>();

        // member id 
        if (memberId != null && !memberId.isEmpty()) {
            criteriaList.add(Criteria.where("memberId").regex(memberId, "i"));
        }

        Criteria criteria = FindAllChatMembersWithCriteria(idSearch, keywords, role, joindDateStart, joinDateEnd);
        if (criteria != null)
            criteriaList.add(criteria);

        return criteriaList.isEmpty() ? null : new Criteria().andOperator(criteriaList);
    }  

    public static Criteria FindChatMembersByChatIdWithCriteria(
        boolean idSearch,
        String chatId,
        String keywords,
        String role, 
        Instant joindDateStart,
        Instant joinDateEnd
    ) 
    {
        List<Criteria> criteriaList = new ArrayList<>();

        // chat id 
        if (chatId != null && !chatId.isEmpty()) {
            criteriaList.add(Criteria.where("chatId").regex(chatId, "i"));
        }

        Criteria criteria = FindAllChatMembersWithCriteria(idSearch, keywords, role, joindDateStart, joinDateEnd);
        if (criteria != null)
            criteriaList.add(criteria);

        return criteriaList.isEmpty() ? null : new Criteria().andOperator(criteriaList);
    }  
}
