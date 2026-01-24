package com.owl.social_service.application.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.owl.social_service.application.admin.ControlBlockAdminServices;
import com.owl.social_service.persistence.mongodb.document.Block;
import com.owl.social_service.persistence.mongodb.repository.BlockRepository;
import com.owl.social_service.presentation.dto.BlockCreateRequest;


@Service
@Transactional
public class ControlBlockUserServices {
    private final ControlBlockAdminServices controlBlockAdminServices;
    private final GetBlockUserServices getBlockUserServices;
    private final BlockRepository blockRepository;

    public ControlBlockUserServices(ControlBlockAdminServices controlBlockAdminServices, GetBlockUserServices getBlockUserServices, BlockRepository blockRepository) {
        this.controlBlockAdminServices = controlBlockAdminServices;
        this.getBlockUserServices = getBlockUserServices;
        this.blockRepository = blockRepository;
    }

    public Block addNewBlock(String requesterId, String blockedId) {
        BlockCreateRequest request = new BlockCreateRequest();
        request.blockerId = requesterId;
        request.blockedId = blockedId;

        return controlBlockAdminServices.addNewBlock(request);
    }

    public void deleteBlock(String requesterId, String id) {
        Block block = getBlockUserServices.getBlockById(requesterId, id);

        if (block == null)
            throw new IllegalArgumentException("Block not found");

        blockRepository.deleteById(id);
    }
}
