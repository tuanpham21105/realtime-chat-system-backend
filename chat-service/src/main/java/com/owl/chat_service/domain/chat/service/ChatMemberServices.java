package com.owl.chat_service.domain.chat.service;

import com.owl.chat_service.persistence.mongodb.document.ChatMember.ChatMemberRole;

public class ChatMemberServices {
    public static int compareRole(ChatMemberRole a, ChatMemberRole b) {
        int an = convertRoleToNumber(a);
        int bn = convertRoleToNumber(b);
        if (an > bn) {
            return 1;
        }
        else if (an < bn) {
            return -1;
        }
        else {
            return 0;
        }
    }

    private static int convertRoleToNumber(ChatMemberRole a) {
        switch (a) {
            case ChatMemberRole.OWNER:
                return 0;
            case ChatMemberRole.ADMIN:
                return -1;
            case ChatMemberRole.MEMBER:
                return -2;
            default:
                return -3;
        }
    }
}
