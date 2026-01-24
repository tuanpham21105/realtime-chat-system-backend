package com.owl.social_service.application.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.owl.social_service.application.admin.ControlFriendRequestAdminServices;
import com.owl.social_service.application.admin.GetFriendRequestAdminServices;
import com.owl.social_service.domain.validate.FriendshipValidate;
import com.owl.social_service.persistence.mongodb.document.FriendRequest;
import com.owl.social_service.persistence.mongodb.document.FriendRequest.FriendRequestStatus;
import com.owl.social_service.presentation.dto.FriendRequestCreateRequest;

@Service
@Transactional
public class ControlFriendRequestUserServices {
    private final ControlFriendRequestAdminServices controlFriendRequestAdminServices;
    private final GetFriendRequestAdminServices getFriendRequestAdminServices;
    
    public ControlFriendRequestUserServices(ControlFriendRequestAdminServices controlFriendRequestAdminServices, GetFriendRequestAdminServices getFriendRequestAdminServices) {
        this.controlFriendRequestAdminServices = controlFriendRequestAdminServices;
        this.getFriendRequestAdminServices = getFriendRequestAdminServices;
    }

    public FriendRequest addNewFriendRequest(String requesterId, String receiverId) {
        FriendRequestCreateRequest request = new FriendRequestCreateRequest();
        request.senderId = requesterId;
        request.receiverId = receiverId;

        return controlFriendRequestAdminServices.addNewFriendRequest(request);
    }

    public FriendRequest updateFriendRequest(String requesterId, String id, String response) {
        if (!FriendshipValidate.validateUserId(requesterId))
            throw new IllegalArgumentException("Invalid requester id");

        FriendRequest friendRequest = getFriendRequestAdminServices.getFriendRequestById(id);

        if (friendRequest == null) 
            throw new IllegalArgumentException("Friend request not found");

        if (friendRequest.getReceiverId().compareTo(requesterId) != 0)
            throw new SecurityException("Requester does not have access to this friend request");

        return controlFriendRequestAdminServices.updateFriendRequestStatus(id, response);
    }

    public void deleteFriendRequest(String requesterId, String id) {
        if (!FriendshipValidate.validateUserId(requesterId))
            throw new IllegalArgumentException("Invalid requester id");

        FriendRequest friendRequest = getFriendRequestAdminServices.getFriendRequestById(id);

        if (friendRequest == null) 
            throw new IllegalArgumentException("Friend request not found");

        if (friendRequest.getSenderId().compareTo(requesterId) != 0)
            throw new SecurityException("Requester does not have access to this friend request");

        if (friendRequest.getStatus() != FriendRequestStatus.PENDING)
            throw new IllegalArgumentException("Friend request have been processed");

        controlFriendRequestAdminServices.deleteFriendRequest(id);
    }
}
