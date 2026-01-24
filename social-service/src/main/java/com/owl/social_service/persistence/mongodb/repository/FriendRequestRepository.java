package com.owl.social_service.persistence.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.owl.social_service.persistence.mongodb.document.FriendRequest;
import java.util.List;


public interface FriendRequestRepository extends MongoRepository<FriendRequest, String> {
    public List<FriendRequest> findBySenderIdAndReceiverId(String senderId, String receiverId);
} 
