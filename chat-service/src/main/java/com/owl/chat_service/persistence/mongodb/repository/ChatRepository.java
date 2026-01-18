package com.owl.chat_service.persistence.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.owl.chat_service.persistence.mongodb.document.Chat;

public interface ChatRepository extends MongoRepository<Chat, String> {

}
