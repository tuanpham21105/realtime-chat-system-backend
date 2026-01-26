package com.owl.social_service.application.admin;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.owl.social_service.domain.validate.FriendshipValidate;
import com.owl.social_service.external_service.client.UserServiceApiClient;
import com.owl.social_service.persistence.mongodb.document.Block;
import com.owl.social_service.persistence.mongodb.document.FriendRequest;
import com.owl.social_service.persistence.mongodb.document.Friendship;
import com.owl.social_service.persistence.mongodb.repository.BlockRepository;
import com.owl.social_service.presentation.dto.BlockCreateRequest;

@Service
@Transactional
public class ControlBlockAdminServices {
    private final BlockRepository blockRepository;
    private final GetBlockAdminServices getBlockAdminServices;
    private final GetFriendRequestAdminServices getFriendRequestAdminServices;
    private final ControlFriendRequestAdminServices controlFriendRequestAdminServices;
    private final GetFriendshipAdminServices getFriendshipAdminServices;
    private final ControlFriendshipAdminServices controlFriendshipAdminServices;
    private final UserServiceApiClient userServiceApiClient;

    public ControlBlockAdminServices(BlockRepository blockRepository, GetBlockAdminServices getBlockAdminServices, GetFriendRequestAdminServices getFriendRequestAdminServices, ControlFriendRequestAdminServices controlFriendRequestAdminServices, ControlFriendshipAdminServices controlFriendshipAdminServices, GetFriendshipAdminServices getFriendshipAdminServices, UserServiceApiClient userServiceApiClient) {
        this.blockRepository = blockRepository;
        this.getBlockAdminServices = getBlockAdminServices;
        this.getFriendRequestAdminServices = getFriendRequestAdminServices;
        this.controlFriendRequestAdminServices = controlFriendRequestAdminServices;
        this.getFriendshipAdminServices = getFriendshipAdminServices;
        this.controlFriendshipAdminServices = controlFriendshipAdminServices;
        this.userServiceApiClient = userServiceApiClient;
    }

    public Block addNewBlock(BlockCreateRequest request) {
        if (!FriendshipValidate.validateUserId(request.blockerId))
            throw new IllegalArgumentException("Invalid blocker id");

        if (!FriendshipValidate.validateUserId(request.blockedId))
            throw new IllegalArgumentException("Invalid blocked id");

        if (userServiceApiClient.getUserById(request.blockerId) != null) 
            throw new IllegalArgumentException("Blocker does not exists");

        if (userServiceApiClient.getUserById(request.blockedId) != null) 
            throw new IllegalArgumentException("Blocked does not exists");

        Block existingBlock = getBlockAdminServices.getUserBlockUser(request.blockerId, request.blockedId);

        if (existingBlock != null)
            throw new IllegalArgumentException("Block already exists");

        Block newBlock = new Block();
        newBlock.setId(UUID.randomUUID().toString());
        newBlock.setBlockerId(request.blockerId);
        newBlock.setBlockedId(request.blockedId);
        newBlock.setCreatedDate(Instant.now());

        List<FriendRequest> friendRequests = getFriendRequestAdminServices.getFriendRequestsByUsersId(request.blockerId, request.blockedId, -1, 0, false, null, "PENDING", null, null, null, null);

        for (FriendRequest friendRequest : friendRequests) {
            controlFriendRequestAdminServices.deleteFriendRequest(friendRequest.getId());
        }

        Friendship friendship = getFriendshipAdminServices.getFriendshipByUsersId(request.blockerId, request.blockedId);

        if (friendship != null)
            controlFriendshipAdminServices.deleteFriendship(friendship.getId());

        blockRepository.save(newBlock);

        return newBlock;
    }

    public void deleteBlock(String id) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("Invalid id");

        Block existingBlock = getBlockAdminServices.getBlockById(id);

        if (existingBlock == null)
            throw new IllegalArgumentException("Block not found");

        blockRepository.deleteById(id);
    }
}
