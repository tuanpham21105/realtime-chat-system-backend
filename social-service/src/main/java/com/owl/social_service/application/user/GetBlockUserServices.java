package com.owl.social_service.application.user;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.owl.social_service.application.admin.GetBlockAdminServices;
import com.owl.social_service.domain.validate.FriendshipValidate;
import com.owl.social_service.persistence.mongodb.document.Block;

@Service
public class GetBlockUserServices {
    private final GetBlockAdminServices getBlockAdminServices;
    public GetBlockUserServices(GetBlockAdminServices getBlockAdminServices) {
        this.getBlockAdminServices = getBlockAdminServices;
    }

    public List<Block> getUserBlocked(
        String requesterId,
        int page,
        int size,
        boolean ascSort,
        Instant createdDateStart,
        Instant createdDateEnd
    )
    {
        if (!FriendshipValidate.validateUserId(requesterId))
            throw new IllegalArgumentException("Invalid requester id");

        return getBlockAdminServices.getUserBlocked(requesterId, page, size, ascSort, createdDateStart, createdDateEnd);
    }

    public Block getBlockById(
        String requesterId,
        String id
    ) 
    {
        if (!FriendshipValidate.validateUserId(requesterId))
            throw new IllegalArgumentException("Invalid requester id");
        
        Block existingBlock = getBlockAdminServices.getBlockById(id);

        if (existingBlock == null)
            throw new IllegalArgumentException("Block not found");

        if (existingBlock.getBlockerId().compareTo(requesterId) != 0)
            throw new SecurityException("Requester does not have permission to access this block");

        return existingBlock;
    }
}
