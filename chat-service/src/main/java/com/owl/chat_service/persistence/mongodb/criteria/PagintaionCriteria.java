package com.owl.chat_service.persistence.mongodb.criteria;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PagintaionCriteria {
    public static Pageable PagableCriteria(int page, int size, boolean ascSort, String properties) {
        if (page < 0 || size < 0) 
            throw new IllegalArgumentException("Page must bigger or equal 0 and size must bigger or equal to 1");

        Sort sort = Sort.by(ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, properties);

        return PageRequest.of(page, size, sort);
    }
}
