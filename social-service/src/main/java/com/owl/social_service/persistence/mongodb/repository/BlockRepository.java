package com.owl.social_service.persistence.mongodb.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.owl.social_service.persistence.mongodb.document.Block;

public interface BlockRepository extends MongoRepository<Block, String> {
    public Optional<Block> findByBlockerIdAndBlockedId(String blockerId, String blockedId);
}
