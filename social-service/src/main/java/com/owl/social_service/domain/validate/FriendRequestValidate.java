package com.owl.social_service.domain.validate;

import com.owl.social_service.persistence.mongodb.document.FriendRequest.FriendRequestStatus;

public class FriendRequestValidate {
    public static boolean validateStatus(String status) {
        if (status == null || status.isBlank()) return false;
        try {
            FriendRequestStatus.valueOf(status);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
