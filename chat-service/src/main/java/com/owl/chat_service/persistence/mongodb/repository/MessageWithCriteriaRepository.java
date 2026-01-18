package com.owl.chat_service.persistence.mongodb.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.owl.chat_service.persistence.mongodb.document.Message;

@Repository
public class MessageWithCriteriaRepository {
    private final MongoTemplate mongoTemplate;

    public MessageWithCriteriaRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Message> findAll(Criteria criteria) {
        if (criteria == null) 
            throw new IllegalArgumentException("Criteria cannot be null");

        Query query = new Query(criteria);

        return mongoTemplate.find(query, Message.class);
    }

    public List<Message> findAll(Sort sort) {
        if (sort == null)
            throw new IllegalArgumentException("Sort cannot be null");

        return mongoTemplate.find(new Query().with(sort), Message.class);
    }

    public List<Message> findAll(Pageable pageable) {
        if (pageable == null)
            throw new IllegalArgumentException("Pageable cannot be null");
        
        Query query = new Query().with(pageable);

        return mongoTemplate.find(query, Message.class);
    }

    public List<Message> findAll(Criteria criteria, Pageable pageable) {
        if (criteria == null) 
            throw new IllegalArgumentException("Criteria cannot be null");

        if (pageable == null)
            throw new IllegalArgumentException("Pageable cannot be null");
        
        Query query = new Query(criteria).with(pageable);

        return mongoTemplate.find(query, Message.class);
    }

    public List<Message> findAll(Criteria criteria, Sort sort) {
        if (criteria == null) 
            throw new IllegalArgumentException("Criteria cannot be null");

        if (sort == null)
            throw new IllegalArgumentException("Sort cannot be null");
        
        Query query = new Query(criteria).with(sort);

        return mongoTemplate.find(query, Message.class);
    }
}
