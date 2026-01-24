package com.owl.social_service.persistence.mongodb.criteria;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import org.springframework.data.mongodb.core.query.Criteria;

import com.owl.social_service.domain.validate.FriendRequestValidate;
import com.owl.social_service.infrastructure.utils.KeywordUtils;

public class FriendRequestCriteria {
    public static Criteria findAll(
        boolean idSearch,
        String keywords,
        String status,
        Instant createdDateStart,
        Instant createdDateEnd,
        Instant updatedDateStart,
        Instant updatedDateEnd
    ) 
    {
        List<Criteria> criteriaList = new ArrayList<>();

        // keyword search (name)
        if (keywords != null && !keywords.isBlank()) {
            List<Criteria> keywordsCriteriaList = new ArrayList<Criteria>();
            for (String keyword : KeywordUtils.parseKeywords(keywords)) {    
                if (keyword == null) continue;
                keywordsCriteriaList.add(
                    Criteria.where("senderId")
                            .regex(Pattern.compile(".*" + Pattern.quote(keyword) + ".*", Pattern.CASE_INSENSITIVE))
                );
                keywordsCriteriaList.add(
                    Criteria.where("receiverId")
                    .regex(Pattern.compile(".*" + Pattern.quote(keyword) + ".*", Pattern.CASE_INSENSITIVE))
                );
                
                if (idSearch) {
                    keywordsCriteriaList.add(
                        Criteria.where("id")
                        .regex(Pattern.compile(".*" + Pattern.quote(keyword) + ".*", Pattern.CASE_INSENSITIVE))
                    );
                }
            }
            criteriaList.add(new Criteria().orOperator(keywordsCriteriaList));
        }

        // status
        if (status != null && FriendRequestValidate.validateStatus(status)) {
            criteriaList.add(Criteria.where("status").is(status));
        }

        // created date range
        if (createdDateStart != null || createdDateEnd != null) {
            Criteria dateCriteria = Criteria.where("createdDate");

            if (createdDateStart != null) {
                dateCriteria.gte(Objects.requireNonNull(createdDateStart, "createdDateStart cannot be null"));
            }
            if (createdDateEnd != null) {
                dateCriteria.lte(Objects.requireNonNull(createdDateEnd, "createdDateEnd cannot be null"));
            }

            criteriaList.add(dateCriteria);
        }

        // updated date range
        if (updatedDateStart != null || updatedDateEnd != null) {
            Criteria dateCriteria = Criteria.where("updatedDate");

            if (updatedDateStart != null) {
                dateCriteria.gte(Objects.requireNonNull(updatedDateStart, "updatedDateStart cannot be null"));
            }
            if (updatedDateEnd != null) {
                dateCriteria.lte(Objects.requireNonNull(updatedDateEnd, "updatedDateEnd cannot be null"));
            }

            criteriaList.add(dateCriteria);
        }

        return criteriaList.isEmpty() ? null : new Criteria().andOperator(criteriaList);
    }

    public static Criteria findAllBySenderId(
        boolean idSearch,
        String senderId,
        String keywords,
        String status,
        Instant createdDateStart,
        Instant createdDateEnd,
        Instant updatedDateStart,
        Instant updatedDateEnd
    ) 
    {
        List<Criteria> criteriaList = new ArrayList<>();
        
        if (senderId != null && !senderId.isEmpty()) {
            criteriaList.add(
                Criteria.where("senderId").is(senderId)
            );
        }

        Criteria criteria = findAll(idSearch, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd);
        if (criteria != null)
            criteriaList.add(criteria);

        return criteriaList.isEmpty() ? null : new Criteria().andOperator(criteriaList);
    }

    public static Criteria findAllByReceiverId(
        boolean idSearch,
        String receiverId,
        String keywords,
        String status,
        Instant createdDateStart,
        Instant createdDateEnd,
        Instant updatedDateStart,
        Instant updatedDateEnd
    ) 
    {
        List<Criteria> criteriaList = new ArrayList<>();
        
        if (receiverId != null && !receiverId.isEmpty()) {
            criteriaList.add(
                Criteria.where("receiverId").is(receiverId)
            );
        }

        Criteria criteria = findAll(idSearch, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd);
        if (criteria != null)
            criteriaList.add(criteria);

        return criteriaList.isEmpty() ? null : new Criteria().andOperator(criteriaList);
    }

    public static Criteria findAllByUserId(
        boolean idSearch,
        String userId,
        String keywords,
        String status,
        Instant createdDateStart,
        Instant createdDateEnd,
        Instant updatedDateStart,
        Instant updatedDateEnd
    ) 
    {
        List<Criteria> criteriaList = new ArrayList<>();
        
        if (userId != null && !userId.isEmpty()) {
            criteriaList.add(new Criteria().orOperator(
                Criteria.where("senderId").is(userId),
                Criteria.where("receiverId").is(userId)
            ));
        }

        Criteria criteria = findAll(idSearch, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd);
        if (criteria != null)
            criteriaList.add(criteria);

        return criteriaList.isEmpty() ? null : new Criteria().andOperator(criteriaList);
    }

    public static Criteria findAllByUsersId(
        boolean idSearch,
        String firstUserId,
        String secondUserId,
        String keywords,
        String status,
        Instant createdDateStart,
        Instant createdDateEnd,
        Instant updatedDateStart,
        Instant updatedDateEnd
    ) 
    {
        List<Criteria> criteriaList = new ArrayList<>();
        
        if (firstUserId != null && !firstUserId.isEmpty()) {
            criteriaList.add(new Criteria().orOperator(
                (new Criteria().andOperator(
                    Criteria.where("senderId").is(firstUserId),
                    Criteria.where("receiverId").is(secondUserId)
                )),
                (new Criteria().andOperator(
                    Criteria.where("senderId").is(secondUserId),
                    Criteria.where("receiverId").is(firstUserId)
                ))
            ));
        }

        Criteria criteria = findAll(idSearch, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd);
        if (criteria != null)
            criteriaList.add(criteria);

        return criteriaList.isEmpty() ? null : new Criteria().andOperator(criteriaList);
    }
}
