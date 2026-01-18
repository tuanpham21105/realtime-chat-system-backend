package com.owl.chat_service.persistence.mongodb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.owl.chat_service.persistence.mongodb.document.ChatMember;

public interface ChatMemberRepository extends MongoRepository<ChatMember, String> {
    public List<ChatMember> findByMemberId(String memberId);

    public List<ChatMember> findByChatId(String chatId);

    public Optional<ChatMember> findByChatIdAndMemberId(String chatId, String memberId);
}
