package com.owl.chat_service.domain.chat.validate;

import com.owl.chat_service.persistence.mongodb.document.ChatMember.ChatMemberRole;

public class ChatMemberValidate {
    public static boolean validateRequesterAndMemberAreSame(String requesterId, String memberId) {
        if (requesterId.compareToIgnoreCase(memberId) != 0) {
            return false;
        }
        return true;
    }

    public static boolean validateMemberId(String memberId) {
        if (memberId == null || memberId.isBlank()) 
            return false;

        return true;
    }

    public static boolean validateChatId(String chatId) {
        if (chatId == null || chatId.isBlank()) 
            return false;

        return true;
    }

    public static boolean validateRole(String role) {
        try {
            ChatMemberRole.valueOf(role);
            return true;
        }
        catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean validateNickname(String nickname) {
        if (nickname == null || nickname.isBlank()) 
            return false;

        return true;
    }
}
