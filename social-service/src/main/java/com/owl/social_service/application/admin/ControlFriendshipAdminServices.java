package com.owl.social_service.application.admin;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.owl.social_service.application.event.CreateFriendshipEvent;
import com.owl.social_service.application.event.EventEmitter;
import com.owl.social_service.application.event.NotifyEvent;
import com.owl.social_service.domain.validate.FriendshipValidate;
import com.owl.social_service.external_service.client.UserServiceApiClient;
import com.owl.social_service.external_service.dto.ChatCreateRequestDto;
import com.owl.social_service.external_service.dto.WsMessageDto;
import com.owl.social_service.persistence.mongodb.document.Friendship;
import com.owl.social_service.persistence.mongodb.repository.FriendshipRepository;
import com.owl.social_service.presentation.dto.FriendshipCreateRequest;

@Service
@Transactional
public class ControlFriendshipAdminServices {
    private final FriendshipRepository friendshipRepository;
    private final GetFriendshipAdminServices getFriendshipAdminServices;
    private final GetBlockAdminServices getBlockAdminServices;
    private final UserServiceApiClient userServiceApiClient;
    private final EventEmitter emitter;

    public ControlFriendshipAdminServices(FriendshipRepository friendshipRepository, GetFriendshipAdminServices getFriendshipAdminServices, GetBlockAdminServices getBlockAdminServices, UserServiceApiClient userServiceApiClient, EventEmitter emitter) {
        this.friendshipRepository = friendshipRepository;
        this.getFriendshipAdminServices = getFriendshipAdminServices;
        this.getBlockAdminServices = getBlockAdminServices;
        this.userServiceApiClient = userServiceApiClient;
        this.emitter = emitter;}

    public Friendship addNewFriendship(FriendshipCreateRequest request) {
        if (!FriendshipValidate.validateUserId(request.firstUserId))
            throw new IllegalArgumentException("Invalid first user id");

        if (!FriendshipValidate.validateUserId(request.secondUserId))
            throw new IllegalArgumentException("Invalid second user id");

        if (userServiceApiClient.getUserById(request.firstUserId) == null || userServiceApiClient.getUserById(request.secondUserId) == null) 
            throw new IllegalArgumentException("One of the users does not exists");

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

        // create chat
        ChatCreateRequestDto chatCreateRequestDto = new ChatCreateRequestDto();
        chatCreateRequestDto.name = "PRIVATE CHAT";
        chatCreateRequestDto.chatMembersId = new ArrayList<String>();
        chatCreateRequestDto.chatMembersId.add(newFriendship.getFirstUserId());
        chatCreateRequestDto.chatMembersId.add(newFriendship.getSecondUserId());

        CreateFriendshipEvent event = new CreateFriendshipEvent("CREATE FRIENDSHIP", newFriendship.getFirstUserId(), chatCreateRequestDto);

        emitter.emit(event);

        WsMessageDto message = new WsMessageDto("FRIENDSHIP", "CREATED", newFriendship);
        NotifyEvent notifyEvent1 = new NotifyEvent("NOTIFY USER", newFriendship.getFirstUserId(), message);
        emitter.emit(notifyEvent1);
        NotifyEvent notifyEvent2 = new NotifyEvent("NOTIFY USER", newFriendship.getSecondUserId(), message);
        emitter.emit(notifyEvent2);

        return newFriendship;
    }

    public void deleteFriendship(String id) {
        if (!FriendshipValidate.validateUserId(id))
            throw new IllegalArgumentException("Invalid id");

        Friendship existingFriendship = getFriendshipAdminServices.getFriendshipById(id);
        if (existingFriendship == null) 
            throw new IllegalArgumentException("Friendship does not exists");

        WsMessageDto message = new WsMessageDto("FRIENDSHIP", "DELETED", existingFriendship);
        NotifyEvent notifyEvent1 = new NotifyEvent("NOTIFY USER", existingFriendship.getFirstUserId(), message);
        emitter.emit(notifyEvent1);
        NotifyEvent notifyEvent2 = new NotifyEvent("NOTIFY USER", existingFriendship.getSecondUserId(), message);
        emitter.emit(notifyEvent2);

        friendshipRepository.deleteById(id);
    }
}
