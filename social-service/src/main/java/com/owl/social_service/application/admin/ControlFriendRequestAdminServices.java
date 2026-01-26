package com.owl.social_service.application.admin;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.owl.social_service.domain.validate.FriendRequestValidate;
import com.owl.social_service.external_service.client.UserServiceApiClient;
import com.owl.social_service.persistence.mongodb.document.FriendRequest;
import com.owl.social_service.persistence.mongodb.document.Friendship;
import com.owl.social_service.persistence.mongodb.document.FriendRequest.FriendRequestStatus;
import com.owl.social_service.persistence.mongodb.repository.FriendRequestRepository;
import com.owl.social_service.presentation.dto.FriendRequestCreateRequest;
import com.owl.social_service.presentation.dto.FriendshipCreateRequest;

@Service
@Transactional
public class ControlFriendRequestAdminServices {
    private final FriendRequestRepository friendRequestRepository;
    private final GetFriendRequestAdminServices getFriendRequestAdminServices;
    private final GetFriendshipAdminServices getFriendshipAdminServices;
    private final ControlFriendshipAdminServices controlFriendshipAdminServices;
    private final GetBlockAdminServices getBlockAdminServices;
    private final UserServiceApiClient userServiceApiClient;

    public ControlFriendRequestAdminServices(FriendRequestRepository friendRequestRepository, GetFriendRequestAdminServices getFriendRequestAdminServices, GetFriendshipAdminServices getFriendshipAdminServices, ControlFriendshipAdminServices controlFriendshipAdminServices, GetBlockAdminServices getBlockAdminServices, UserServiceApiClient userServiceApiClient) {
        this.friendRequestRepository = friendRequestRepository;
        this.getFriendRequestAdminServices = getFriendRequestAdminServices;
        this.getFriendshipAdminServices = getFriendshipAdminServices;
        this.controlFriendshipAdminServices = controlFriendshipAdminServices;
        this.getBlockAdminServices = getBlockAdminServices;
        this.userServiceApiClient = userServiceApiClient;
    }

    public FriendRequest addNewFriendRequest(FriendRequestCreateRequest request) {
        List<FriendRequest> existingFriendRequest = getFriendRequestAdminServices.getFriendRequestsByUsersId(request.senderId, request.receiverId, -1, 0, true, null, "PENDING", null, null, null, null);

        if (request.senderId.trim().compareToIgnoreCase(request.receiverId.trim()) == 0) 
            throw new IllegalArgumentException("Invalid request");

        if (userServiceApiClient.getUserById(request.senderId) != null) 
            throw new IllegalArgumentException("Sender does not exists");

        if (userServiceApiClient.getUserById(request.receiverId) != null) 
            throw new IllegalArgumentException("Receiver does not exists");

        if (existingFriendRequest.size() > 0) 
            throw new IllegalArgumentException("Friend request already exists");

        Friendship existingFriendship = getFriendshipAdminServices.getFriendshipByUsersId(request.senderId, request.receiverId);

        if (existingFriendship != null) 
            throw new IllegalArgumentException("Friendship already exists");

        if (getBlockAdminServices.getUserBlockUser(request.senderId, request.receiverId) != null)
            throw new IllegalArgumentException("Requester have block this user");

        if (getBlockAdminServices.getUserBlockUser(request.receiverId, request.senderId) != null)
            throw new IllegalArgumentException("This user have block requester");

        FriendRequest newFriendRequest = new FriendRequest();
        newFriendRequest.setId(UUID.randomUUID().toString());
        newFriendRequest.setSenderId(request.senderId);
        newFriendRequest.setReceiverId(request.receiverId);
        newFriendRequest.setStatus(FriendRequestStatus.PENDING);
        newFriendRequest.setCreatedDate(Instant.now());
        newFriendRequest.setUpdatedDate(newFriendRequest.getCreatedDate());

        friendRequestRepository.save(newFriendRequest);

        return newFriendRequest;
    }

    public FriendRequest updateFriendRequestStatus(String id, String status) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("Invalid id");

        FriendRequest friendRequest = getFriendRequestAdminServices.getFriendRequestById(id);

        if (friendRequest == null) 
            throw new IllegalArgumentException("Friend request not found");

        if (friendRequest.getStatus() != FriendRequestStatus.PENDING)
            throw new IllegalArgumentException("Friend request has been processed");

        if (!FriendRequestValidate.validateStatus(status))
            throw new IllegalArgumentException("Invalid status");

        FriendRequestStatus trueStatus = FriendRequestStatus.valueOf(status);

        if (trueStatus == FriendRequestStatus.ACCEPTED) {
            FriendshipCreateRequest request = new FriendshipCreateRequest();
            request.firstUserId = friendRequest.getSenderId();
            request.secondUserId = friendRequest.getReceiverId();
            controlFriendshipAdminServices.addNewFriendship(request);
        }

        friendRequest.setStatus(trueStatus);

        friendRequestRepository.save(friendRequest);

        return friendRequest;
        
    }

    public void deleteFriendRequest(String id) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("Invalid id");

        FriendRequest friendRequest = getFriendRequestAdminServices.getFriendRequestById(id);

        if (friendRequest == null) 
            throw new IllegalArgumentException("Friend request not found");

        friendRequestRepository.deleteById(id);
    }
}
