package com.owl.social_service.domain.validate;

public class FriendshipValidate {
    public static boolean validateUserId(String userId) {
        if (userId == null || userId.isBlank())
            return false;
        
        return true;
    }
}
