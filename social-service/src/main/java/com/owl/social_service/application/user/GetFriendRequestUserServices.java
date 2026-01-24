package com.owl.social_service.application.user;

import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.owl.social_service.application.admin.GetFriendRequestAdminServices;
import com.owl.social_service.persistence.mongodb.criteria.FriendRequestCriteria;
import com.owl.social_service.persistence.mongodb.document.FriendRequest;

@Service
public class GetFriendRequestUserServices {
    private final GetFriendRequestAdminServices getFriendRequestAdminServices;

    public GetFriendRequestUserServices(GetFriendRequestAdminServices getFriendRequestAdminServices) {
        this.getFriendRequestAdminServices = getFriendRequestAdminServices;
    }

    public List<FriendRequest> getFriendRequests(
        String requesterId,
        int page,
        int size,
        boolean ascSort,
        String keywords,
        String status,
        Instant createdDateStart,
        Instant createdDateEnd,
        Instant updatedDateStart,
        Instant updatedDateEnd
    ) 
    {
        if (requesterId == null || requesterId.isBlank())
            throw new IllegalArgumentException("Invalid requester id");

        Criteria criteria = FriendRequestCriteria.findAllByUserId(false, requesterId, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd);

        return getFriendRequestAdminServices.getFriendRequestsWithCriteria(criteria, page, size, ascSort);
    }

    public FriendRequest getFriendRequestById(
        String requesterId,
        String id
    ) 
    {
        if (requesterId == null || requesterId.isBlank())
            throw new IllegalArgumentException("Invalid requester id");

        if (id == null || id.isBlank())
            throw new IllegalArgumentException("Invalid id");
        
        FriendRequest friendRequest = getFriendRequestAdminServices.getFriendRequestById(id);

        if (friendRequest == null)
            throw new IllegalArgumentException("Friend request not found");

        if (friendRequest.getSenderId() != requesterId && friendRequest.getReceiverId() != requesterId)
            throw new SecurityException("Requester does not have permission to access this friend request");

        return friendRequest;
    }

    public List<FriendRequest> getSendFriendRequests(
        String requesterId,
        int page,
        int size,
        boolean ascSort,
        String keywords,
        String status,
        Instant createdDateStart,
        Instant createdDateEnd,
        Instant updatedDateStart,
        Instant updatedDateEnd
    ) 
    {
        if (requesterId == null || requesterId.isBlank())
            throw new IllegalArgumentException("Invalid requester id");

        Criteria criteria = FriendRequestCriteria.findAllBySenderId(false, requesterId, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd);

        return getFriendRequestAdminServices.getFriendRequestsWithCriteria(criteria, page, size, ascSort);
    }

    public List<FriendRequest> getReceiveFriendRequests(
        String requesterId,
        int page,
        int size,
        boolean ascSort,
        String keywords,
        String status,
        Instant createdDateStart,
        Instant createdDateEnd,
        Instant updatedDateStart,
        Instant updatedDateEnd
    ) 
    {
        if (requesterId == null || requesterId.isBlank())
            throw new IllegalArgumentException("Invalid requester id");

        Criteria criteria = FriendRequestCriteria.findAllByReceiverId(false, requesterId, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd);

        return getFriendRequestAdminServices.getFriendRequestsWithCriteria(criteria, page, size, ascSort);
    }
    
    public List<FriendRequest> getFriendRequestsWithUserId(
        String requesterId,
        String userId,
        int page,
        int size,
        boolean ascSort,
        String keywords,
        String status,
        Instant createdDateStart,
        Instant createdDateEnd,
        Instant updatedDateStart,
        Instant updatedDateEnd
    ) 
    {
        if (requesterId == null || requesterId.isBlank())
            throw new IllegalArgumentException("Invalid requester id");

        if (userId == null || userId.isBlank())
            throw new IllegalArgumentException("Invalid user id");

        Criteria criteria = FriendRequestCriteria.findAllByUsersId(false, requesterId, userId, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd);

        return getFriendRequestAdminServices.getFriendRequestsWithCriteria(criteria, page, size, ascSort);
    }

    public List<FriendRequest> getFriendRequestFromRequesterToUser(String requesterId, String receiverId) {
        if (requesterId == null || requesterId.isBlank())
            throw new IllegalArgumentException("Invalid requester id");

        if (receiverId == null || receiverId.isBlank())
            throw new IllegalArgumentException("Invalid receiver id");

        return getFriendRequestAdminServices.getFriendRequestFromUserToUser(requesterId, receiverId);
    }

    public List<FriendRequest> getFriendRequestFromUserToRequester(String requesterId, String senderId) {
        if (requesterId == null || requesterId.isBlank())
            throw new IllegalArgumentException("Invalid requester id");

        if (senderId == null || senderId.isBlank())
            throw new IllegalArgumentException("Invalid senderId id");

        return getFriendRequestAdminServices.getFriendRequestFromUserToUser(senderId, requesterId);
    }
}
