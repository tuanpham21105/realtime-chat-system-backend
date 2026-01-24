package com.owl.social_service.persistence.mongodb.criteria;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import org.springframework.data.mongodb.core.query.Criteria;

import com.owl.social_service.infrastructure.utils.KeywordUtils;

public class BlockCriteria {
    public static Criteria findAll(
        boolean idSearch,
        String keywords,
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
        
                if (idSearch) {
                    keywordsCriteriaList.add(
                        Criteria.where("id")
                        .regex(Pattern.compile(".*" + Pattern.quote(keyword) + ".*", Pattern.CASE_INSENSITIVE))
                    );
                }
            }
            criteriaList.add(new Criteria().orOperator(keywordsCriteriaList));
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

        return criteriaList.isEmpty() ? null : new Criteria().andOperator(criteriaList);
    }

    public static Criteria findByBlockerId(
        boolean idSearch,
        String blockerId,
        String keywords,
        Instant createdDateStart,
        Instant createdDateEnd
    ) 
    {
        List<Criteria> criteriaList = new ArrayList<>();
        
        if (blockerId != null && !blockerId.isEmpty()) {
            criteriaList.add(
                Criteria.where("blockerId").is(blockerId)
            );
        }

        Criteria criteria = findAll(idSearch, keywords, createdDateStart, createdDateEnd);
        if (criteria != null)
            criteriaList.add(criteria);

        return criteriaList.isEmpty() ? null : new Criteria().andOperator(criteriaList);
    }

    public static Criteria findByBlockedId(
        boolean idSearch,
        String blockedId,
        String keywords,
        Instant createdDateStart,
        Instant createdDateEnd
    ) 
    {
        List<Criteria> criteriaList = new ArrayList<>();
        
        if (blockedId != null && !blockedId.isEmpty()) {
            criteriaList.add(
                Criteria.where("blockedId").is(blockedId)
            );
        }

        Criteria criteria = findAll(idSearch, keywords, createdDateStart, createdDateEnd);
        if (criteria != null)
            criteriaList.add(criteria);

        return criteriaList.isEmpty() ? null : new Criteria().andOperator(criteriaList);
    }
}
