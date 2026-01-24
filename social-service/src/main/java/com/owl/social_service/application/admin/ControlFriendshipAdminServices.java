package com.owl.social_service.application.admin;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.owl.social_service.domain.validate.FriendshipValidate;
import com.owl.social_service.persistence.mongodb.document.Friendship;
import com.owl.social_service.persistence.mongodb.repository.FriendshipRepository;
import com.owl.social_service.presentation.dto.FriendshipCreateRequest;

@Service
@Transactional
public class ControlFriendshipAdminServices {
    private final FriendshipRepository friendshipRepository;
    private final GetFriendshipAdminServices getFriendshipAdminServices;
    private final GetBlockAdminServices getBlockAdminServices;

    public ControlFriendshipAdminServices(FriendshipRepository friendshipRepository, GetFriendshipAdminServices getFriendshipAdminServices, GetBlockAdminServices getBlockAdminServices) {
        this.friendshipRepository = friendshipRepository;
        this.getFriendshipAdminServices = getFriendshipAdminServices;
        this.getBlockAdminServices = getBlockAdminServices;}

    public Friendship addNewFriendship(FriendshipCreateRequest request) {
        if (!FriendshipValidate.validateUserId(request.firstUserId))
            throw new IllegalArgumentException("Invalid first user id");

        if (!FriendshipValidate.validateUserId(request.secondUserId))
            throw new IllegalArgumentException("Invalid second user id");

        if (getFriendshipAdminServices.getFriendshipByUsersId(request.firstUserId, request.secondUserId) != null) 
            throw new IllegalArgumentException("Friendship already exists");

        if (getBlockAdminServices.getUserBlockUser(request.firstUserId, request.secondUserId) != null)
            throw new IllegalArgumentException("Have been blocked");

        if (getBlockAdminServices.getUserBlockUser(request.secondUserId, request.firstUserId) != null)
            throw new IllegalArgumentException("Have been blocked");

        Friendship newFriendship = new Friendship();
        newFriendship.setId(UUID.randomUUID().toString());
        newFriendship.setFirstUserId(request.firstUserId);
        newFriendship.setSecondUserId(request.secondUserId);
        newFriendship.setCreatedDate(Instant.now());

        friendshipRepository.save(newFriendship);

        return newFriendship;
    }

    public void deleteFriendship(String id) {
        if (!FriendshipValidate.validateUserId(id))
            throw new IllegalArgumentException("Invalid id");

        if (getFriendshipAdminServices.getFriendshipById(id) == null) 
            throw new IllegalArgumentException("Friendship does not exists");

        friendshipRepository.deleteById(id);
    }
}
