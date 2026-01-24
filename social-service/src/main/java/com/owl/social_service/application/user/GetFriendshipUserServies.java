package com.owl.social_service.application.user;

import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.owl.social_service.application.admin.GetFriendshipAdminServices;
import com.owl.social_service.domain.validate.FriendshipValidate;
import com.owl.social_service.persistence.mongodb.criteria.FriendshipCriteria;
import com.owl.social_service.persistence.mongodb.document.Friendship;

@Service
public class GetFriendshipUserServies {
    private final GetFriendshipAdminServices getFriendshipAdminServices;

    public GetFriendshipUserServies(GetFriendshipAdminServices getFriendshipAdminServices) {
        this.getFriendshipAdminServices = getFriendshipAdminServices;
    }

    public List<Friendship> getFriendshipsOfUser(
        String requesterId,
        int page,
        int size,
        boolean ascSort,
        // String keywords,
        Instant createdDateStart,
        Instant createdDateEnd
    ) 
    {
        if (!FriendshipValidate.validateUserId(requesterId))
            throw new IllegalArgumentException("Invalid requester id");

        Criteria criteria = FriendshipCriteria.findByUserId(false, requesterId, null, createdDateStart, createdDateEnd);

        return getFriendshipAdminServices.getFriendshipWithCriteria(criteria, page, size, ascSort);
    }

    public Friendship getFrienshipById(String requesterId, String id) {
        if (!FriendshipValidate.validateUserId(requesterId))
            throw new IllegalArgumentException("Invalid requester id");

        if (!FriendshipValidate.validateUserId(id))
            throw new IllegalArgumentException("Invalid id");

        Friendship friendship = getFriendshipAdminServices.getFriendshipById(id);
        if (friendship == null) 
            throw new IllegalArgumentException("Friendship not found");

        if (friendship.getFirstUserId().compareTo(requesterId) != 0 && friendship.getSecondUserId().compareTo(requesterId) != 0) 
            throw new SecurityException("Requester does not have access to this friendship");

        return friendship;
    } 

    public Friendship getFriendshipWithUser(String requesterId, String userId) {
        if (!FriendshipValidate.validateUserId(requesterId))
            throw new IllegalArgumentException("Invalid requester id");

        if (!FriendshipValidate.validateUserId(userId))
            throw new IllegalArgumentException("Invalid user id");
        
        Friendship friendship = getFriendshipAdminServices.getFriendshipByUsersId(requesterId, userId);
        if (friendship == null) 
            throw new IllegalArgumentException("Friendship not found");

        return friendship;
    }
}
