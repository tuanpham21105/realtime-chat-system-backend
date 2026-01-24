package com.owl.social_service.persistence.mongodb.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class CriteriaRepository {
    private final MongoTemplate mongoTemplate;

    public CriteriaRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public <T> List<T> findAll(Class<T> entityClass, Criteria criteria) {
        if (entityClass == null)
            throw new IllegalArgumentException("entityClass cannot be null");

        if (criteria == null)
            throw new IllegalArgumentException("Criteria cannot be null");

        Query query = new Query(criteria);

        return mongoTemplate.find(query, entityClass);
    }

    public <T> List<T> findAll(Class<T> entityClass, Sort sort) {
        if (entityClass == null)
            throw new IllegalArgumentException("entityClass cannot be null");

        if (sort == null)
            throw new IllegalArgumentException("Sort cannot be null");

        return mongoTemplate.find(new Query().with(sort), entityClass);
    }

    public <T> List<T> findAll(Class<T> entityClass, Pageable pageable) {
        if (entityClass == null)
            throw new IllegalArgumentException("entityClass cannot be null");

        if (pageable == null)
            throw new IllegalArgumentException("Pageable cannot be null");

        Query query = new Query().with(pageable);

        return mongoTemplate.find(query, entityClass);
    }

    public <T> List<T> findAll(Class<T> entityClass, Criteria criteria, Pageable pageable) {
        if (entityClass == null)
            throw new IllegalArgumentException("entityClass cannot be null");

        if (criteria == null)
            throw new IllegalArgumentException("Criteria cannot be null");

        if (pageable == null)
            throw new IllegalArgumentException("Pageable cannot be null");

        Query query = new Query(criteria).with(pageable);

        return mongoTemplate.find(query, entityClass);
    }

    public <T> List<T> findAll(Class<T> entityClass, Criteria criteria, Sort sort) {
        if (entityClass == null)
            throw new IllegalArgumentException("entityClass cannot be null");

        if (criteria == null)
            throw new IllegalArgumentException("Criteria cannot be null");

        if (sort == null)
            throw new IllegalArgumentException("Sort cannot be null");

        Query query = new Query(criteria).with(sort);

        return mongoTemplate.find(query, entityClass);
    }
}
