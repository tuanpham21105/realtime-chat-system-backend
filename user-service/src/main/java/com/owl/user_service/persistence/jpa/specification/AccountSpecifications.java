package com.owl.user_service.persistence.jpa.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.owl.user_service.persistence.jpa.entity.Account;

import jakarta.persistence.criteria.Predicate;

public class AccountSpecifications {

    public static Specification<Account> findAccountSpecification(List<String> keywords, int status) {
        return (root, query, cb) -> {
            query.distinct(true); // remove duplicates

            List<Predicate> predicates = new ArrayList<>();

            if (keywords == null || keywords.isEmpty()) {
                
            }
            else {

                for (String kw : keywords) {
                    String pattern = "%" + kw.toLowerCase() + "%";
                    predicates.add(cb.like(cb.lower(root.get("id")), pattern));
                    predicates.add(cb.like(cb.lower(root.get("username")), pattern));
                }
            }

            if (status == 1) {
                predicates.add(cb.isTrue(root.get("status")));
            } else if (status == -1) {
                predicates.add(cb.isFalse(root.get("status")));
            }

            // combine all predicates with OR
            return cb.or(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }
}

