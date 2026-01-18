package com.owl.chat_service.persistence.mongodb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.owl.chat_service.persistence.mongodb.document.Message;

public interface MessageRepository extends MongoRepository<Message, String> {
    public List<Message> findByChatId(String chatId);
}
