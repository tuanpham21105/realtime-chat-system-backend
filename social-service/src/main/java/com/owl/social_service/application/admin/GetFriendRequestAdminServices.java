package com.owl.social_service.application.admin;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.owl.social_service.domain.validate.FriendshipValidate;
import com.owl.social_service.persistence.mongodb.criteria.FriendRequestCriteria;
import com.owl.social_service.persistence.mongodb.criteria.PagintaionCriteria;
import com.owl.social_service.persistence.mongodb.document.FriendRequest;
import com.owl.social_service.persistence.mongodb.repository.CriteriaRepository;
import com.owl.social_service.persistence.mongodb.repository.FriendRequestRepository;

@Service
public class GetFriendRequestAdminServices {
    private final FriendRequestRepository friendRequestRepository;
    private final CriteriaRepository criteriaRepository;

    public GetFriendRequestAdminServices(FriendRequestRepository friendRequestRepository, CriteriaRepository criteriaRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.criteriaRepository = criteriaRepository;
    }

    public List<FriendRequest> getFriendRequestsWithCriteria(Criteria criteria, int page, int size, boolean ascSort) {
        Sort sort = Sort.by(ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, "updatedDate");

        if (page == -1) {
            if (criteria != null)
                return criteriaRepository.findAll(FriendRequest.class, criteria, sort);
            else
                return criteriaRepository.findAll(FriendRequest.class, sort);
        }

        Pageable pageable = PagintaionCriteria.PagableCriteria(page, size, ascSort, "updatedDate");

        if (criteria != null)
            return criteriaRepository.findAll(FriendRequest.class, criteria, pageable);
        else 
            return criteriaRepository.findAll(FriendRequest.class, pageable);
    }

    public List<FriendRequest> getFriendRequests(
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
        Criteria criteria = FriendRequestCriteria.findAll(true, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd);

        return getFriendRequestsWithCriteria(criteria, page, size, ascSort);
    }

    public FriendRequest getFriendRequestById(String id) {
        if (!FriendshipValidate.validateUserId(id)) 
            throw new IllegalArgumentException("Invalid id");

        return friendRequestRepository.findById(id).orElse(null);
    }

    public List<FriendRequest> getFriendRequestsBySenderId(
        String senderId,
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
        if (!FriendshipValidate.validateUserId(senderId)) 
            throw new IllegalArgumentException("Invalid sender id");
        Criteria criteria = FriendRequestCriteria.findAllBySenderId(true, senderId, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd);

        return getFriendRequestsWithCriteria(criteria, page, size, ascSort);
    }

    public List<FriendRequest> getFriendRequestsByReceiverId(
        String receiverId,
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
        if (!FriendshipValidate.validateUserId(receiverId)) 
            throw new IllegalArgumentException("Invalid receiver id");
        Criteria criteria = FriendRequestCriteria.findAllByReceiverId(true, receiverId, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd);

        return getFriendRequestsWithCriteria(criteria, page, size, ascSort);
    }

    public List<FriendRequest> getFriendRequestsByUserId(
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
        if (!FriendshipValidate.validateUserId(userId)) 
            throw new IllegalArgumentException("Invalid user id");
        Criteria criteria = FriendRequestCriteria.findAllByUserId(true, userId, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd);

        return getFriendRequestsWithCriteria(criteria, page, size, ascSort);
    }

    public List<FriendRequest> getFriendRequestsByUsersId(
        String firstUserId,
        String secondUserId,
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
        if (!FriendshipValidate.validateUserId(firstUserId)) 
            throw new IllegalArgumentException("Invalid first user id");

        if (!FriendshipValidate.validateUserId(secondUserId)) 
            throw new IllegalArgumentException("Invalid second user id");
        Criteria criteria = FriendRequestCriteria.findAllByUsersId(true, firstUserId, secondUserId, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd);

        return getFriendRequestsWithCriteria(criteria, page, size, ascSort);
    }

    public List<FriendRequest> getFriendRequestFromUserToUser(String senderId, String receiverId) {
        if (!FriendshipValidate.validateUserId(senderId)) 
            throw new IllegalArgumentException("Invalid sender id");

        if (!FriendshipValidate.validateUserId(receiverId)) 
            throw new IllegalArgumentException("Invalid receiver id");

        return friendRequestRepository.findBySenderIdAndReceiverId(senderId, receiverId);   
    }
}
