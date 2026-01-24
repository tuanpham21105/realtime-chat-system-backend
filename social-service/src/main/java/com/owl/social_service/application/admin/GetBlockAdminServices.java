package com.owl.social_service.application.admin;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.owl.social_service.persistence.mongodb.criteria.BlockCriteria;
import com.owl.social_service.persistence.mongodb.criteria.PagintaionCriteria;
import com.owl.social_service.persistence.mongodb.document.Block;
import com.owl.social_service.persistence.mongodb.repository.BlockRepository;
import com.owl.social_service.persistence.mongodb.repository.CriteriaRepository;

@Service
public class GetBlockAdminServices {
    private final BlockRepository blockRepository;
    private final CriteriaRepository criteriaRepository;

    public GetBlockAdminServices(BlockRepository blockRepository, CriteriaRepository criteriaRepository) {
        this.blockRepository = blockRepository;
        this.criteriaRepository = criteriaRepository;
    }

    public List<Block> getBlocksWithCriteria(Criteria criteria, int page, int size, boolean ascSort) {
        Sort sort = Sort.by(ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, "createdDate");

        if (page == -1) {
            if (criteria == null)
                return criteriaRepository.findAll(Block.class, sort);
            else 
                return criteriaRepository.findAll(Block.class, criteria, sort);
        }

        Pageable pageable = PagintaionCriteria.PagableCriteria(page, size, ascSort, "createdDate");

        if (criteria == null)
            return criteriaRepository.findAll(Block.class, pageable);
        else 
            return criteriaRepository.findAll(Block.class, criteria, pageable);
    }

    public List<Block> getBlocks(
        int page,
        int size,
        boolean ascSort,
        Instant createdDateStart,
        Instant createdDateEnd
    ) 
    {
        Criteria criteria = BlockCriteria.findAll(false, null, createdDateStart, createdDateEnd);

        return getBlocksWithCriteria(criteria, page, size, ascSort);
    }

    public Block getBlockById(String id) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("Invalid id");

        return blockRepository.findById(id).orElse(null);
    }

    public List<Block> getUserBlocked(
        String userId,
        int page,
        int size,
        boolean ascSort,
        Instant createdDateStart,
        Instant createdDateEnd
    )
    {
        if (userId == null || userId.isBlank())
            throw new IllegalArgumentException("Invalid user id");
        
        Criteria criteria = BlockCriteria.findByBlockerId(false, userId, null, createdDateStart, createdDateEnd);

        return getBlocksWithCriteria(criteria, page, size, ascSort);
    }

    public List<Block> getUserBlocker(
        String userId,
        int page,
        int size,
        boolean ascSort,
        Instant createdDateStart,
        Instant createdDateEnd
    )
    {
        if (userId == null || userId.isBlank())
            throw new IllegalArgumentException("Invalid user id");
        
        Criteria criteria = BlockCriteria.findByBlockedId(false, userId, null, createdDateStart, createdDateEnd);

        return getBlocksWithCriteria(criteria, page, size, ascSort);
    }

    public Block getUserBlockUser(String blockerId, String blockedId) {
        if (blockerId == null || blockerId.isBlank())
            throw new IllegalArgumentException("Invalid blocker id");

        if (blockedId == null || blockedId.isBlank())
            throw new IllegalArgumentException("Invalid blocked id");

        return blockRepository.findByBlockerIdAndBlockedId(blockerId, blockedId).orElse(null);
    }
}
