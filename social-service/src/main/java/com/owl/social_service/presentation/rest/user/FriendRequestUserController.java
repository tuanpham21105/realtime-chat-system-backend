package com.owl.social_service.presentation.rest.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.owl.social_service.application.user.ControlFriendRequestUserServices;
import com.owl.social_service.application.user.GetFriendRequestUserServices;
import com.owl.social_service.presentation.dto.FriendRequestCreateUserRequest;
import com.owl.social_service.presentation.dto.FriendRequestResponseRequest;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/friend-request")
public class FriendRequestUserController {
    private final GetFriendRequestUserServices getFriendRequestUserServices;
    private final ControlFriendRequestUserServices controlFriendRequestUserServices;

    public FriendRequestUserController(GetFriendRequestUserServices getFriendRequestUserServices, ControlFriendRequestUserServices controlFriendRequestUserServices) {
        this.getFriendRequestUserServices = getFriendRequestUserServices;
        this.controlFriendRequestUserServices = controlFriendRequestUserServices;
    }

    @GetMapping("")
    public ResponseEntity<?> getFriendRequests(
        @RequestHeader String requesterId,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "true") boolean ascSort,
        @RequestParam(required = false) String keywords,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) Instant createdDateStart,
        @RequestParam(required = false) Instant createdDateEnd,
        @RequestParam(required = false) Instant updatedDateStart,
        @RequestParam(required = false) Instant updatedDateEnd
    ) 
    {
        try {
            return ResponseEntity.ok().body(getFriendRequestUserServices.getFriendRequests(requesterId, page, size, ascSort, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFriendRequestById(
        @RequestHeader String requesterId,
        @PathVariable String id
    ) 
    {
        try {
            return ResponseEntity.ok().body(getFriendRequestUserServices.getFriendRequestById(requesterId, id));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/send")
    public ResponseEntity<?> getSendFriendRequests(
        @RequestHeader String requesterId,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "true") boolean ascSort,
        @RequestParam(required = false) String keywords,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) Instant createdDateStart,
        @RequestParam(required = false) Instant createdDateEnd,
        @RequestParam(required = false) Instant updatedDateStart,
        @RequestParam(required = false) Instant updatedDateEnd
    ) 
    {
        try {
            return ResponseEntity.ok().body(getFriendRequestUserServices.getSendFriendRequests(requesterId, page, size, ascSort, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/receive")
    public ResponseEntity<?> getReceiveFriendRequests(
        @RequestHeader String requesterId,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "true") boolean ascSort,
        @RequestParam(required = false) String keywords,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) Instant createdDateStart,
        @RequestParam(required = false) Instant createdDateEnd,
        @RequestParam(required = false) Instant updatedDateStart,
        @RequestParam(required = false) Instant updatedDateEnd
    ) 
    {
        try {
            return ResponseEntity.ok().body(getFriendRequestUserServices.getReceiveFriendRequests(requesterId, page, size, ascSort, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getFriendRequestsWithUser(
        @RequestHeader String requesterId,
        @PathVariable String userId,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "true") boolean ascSort,
        @RequestParam(required = false) String keywords,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) Instant createdDateStart,
        @RequestParam(required = false) Instant createdDateEnd,
        @RequestParam(required = false) Instant updatedDateStart,
        @RequestParam(required = false) Instant updatedDateEnd
    ) 
    {
        try {
            return ResponseEntity.ok().body(getFriendRequestUserServices.getFriendRequestsWithUserId(requesterId, userId, page, size, ascSort, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/receiver/{receiverId}")
    public ResponseEntity<?> getFriendRequestFromRequesterToUser(
        @RequestHeader String requesterId,
        @PathVariable String receiverId
    ) 
    {
        try {
            return ResponseEntity.ok().body(getFriendRequestUserServices.getFriendRequestFromRequesterToUser(requesterId, receiverId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/sender/{senderId}")
    public ResponseEntity<?> getFriendRequestFromUserToRequester(
        @RequestHeader String requesterId,
        @PathVariable String senderId
    ) 
    {
        try {
            return ResponseEntity.ok().body(getFriendRequestUserServices.getFriendRequestFromUserToRequester(requesterId, senderId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("")
    public ResponseEntity<?> postFriendRequest(@RequestHeader String requesterId, @RequestBody FriendRequestCreateUserRequest request) {
        try {
            return ResponseEntity.ok().body(controlFriendRequestUserServices.addNewFriendRequest(requesterId, request.receiverId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/response")
    public ResponseEntity<?> patchFriendRequestStatus(@RequestHeader String requesterId, @PathVariable String id, @RequestBody FriendRequestResponseRequest request) {
        try {
            return ResponseEntity.ok().body(controlFriendRequestUserServices.updateFriendRequest(requesterId, id, request.response));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFriendRequest(@RequestHeader String requesterId, @PathVariable String id) 
    {
        try {
            controlFriendRequestUserServices.deleteFriendRequest(requesterId, id);
            return ResponseEntity.ok().body("Friend request deleted successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
}
