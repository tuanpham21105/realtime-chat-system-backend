package com.owl.social_service.persistence.mongodb.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.owl.social_service.persistence.mongodb.document.Friendship;

public interface FriendshipRepository extends MongoRepository<Friendship, String> {
    public Optional<Friendship> findByFirstUserIdAndSecondUserId(String firstUserId, String secondUserId);
}
