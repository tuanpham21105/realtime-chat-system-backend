package com.owl.social_service.application.admin;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.owl.social_service.domain.validate.FriendshipValidate;
import com.owl.social_service.persistence.mongodb.criteria.FriendshipCriteria;
import com.owl.social_service.persistence.mongodb.criteria.PagintaionCriteria;
import com.owl.social_service.persistence.mongodb.document.Friendship;
import com.owl.social_service.persistence.mongodb.repository.CriteriaRepository;
import com.owl.social_service.persistence.mongodb.repository.FriendshipRepository;

@Service
public class GetFriendshipAdminServices {
    private final FriendshipRepository friendshipRepository;
    private final CriteriaRepository criteriaRepository;

    public GetFriendshipAdminServices(CriteriaRepository criteriaRepository, FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
        this.criteriaRepository = criteriaRepository;
    }

    public List<Friendship> getFriendshipWithCriteria(Criteria criteria, int page, int size, boolean ascSort) {
        Sort sort = Sort.by(ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, "id");

        if (page == -1){
            if (criteria != null)
                return criteriaRepository.findAll(Friendship.class, criteria, sort);
            else
                return criteriaRepository.findAll(Friendship.class, sort);
        }

        Pageable pageable = PagintaionCriteria.PagableCriteria(page, size, ascSort, "id");
    
        if (criteria != null)
            return criteriaRepository.findAll(Friendship.class, criteria, pageable);
        else
            return criteriaRepository.findAll(Friendship.class, pageable);
    }
    
    public List<Friendship> getFriendships(
        int page,
        int size,
        boolean ascSort,
        String keywords,
        Instant createdDateStart,
        Instant createdDateEnd
    ) 
    {
        Criteria criteria = FriendshipCriteria.findAll(true, keywords, createdDateStart, createdDateEnd);

        return getFriendshipWithCriteria(criteria, page, size, ascSort);
    }

    public List<Friendship> getFriendshipsByUserId(
        String userId,
        int page,
        int size,
        boolean ascSort,
        String keywords,
        Instant createdDateStart,
        Instant createdDateEnd
    ) 
    {
        if (!FriendshipValidate.validateUserId(userId))
            throw new IllegalArgumentException("Invalid user id");

        Criteria criteria = FriendshipCriteria.findByUserId(true, userId, keywords, createdDateStart, createdDateEnd);

        return getFriendshipWithCriteria(criteria, page, size, ascSort);
    }

    public Friendship getFriendshipByUsersId(
        String firstUserId,
        String secondUserId
    ) 
    {
        if (!FriendshipValidate.validateUserId(firstUserId))
            throw new IllegalArgumentException("Invalid first user id");

        if (!FriendshipValidate.validateUserId(secondUserId))
            throw new IllegalArgumentException("Invalid second user id");

        return friendshipRepository.findByFirstUserIdAndSecondUserId(firstUserId, secondUserId).orElse(friendshipRepository.findByFirstUserIdAndSecondUserId(secondUserId, firstUserId).orElse(null));
    }

    public Friendship getFriendshipById(String id) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("Invalid id");

        return friendshipRepository.findById(id).orElse(null);
    }
}
