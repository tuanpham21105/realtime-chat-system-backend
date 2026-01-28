package com.chat_system.api_gateway.presentation.rest.friend_request;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chat_system.api_gateway.external_service.client.FriendRequestUserWebClient;
import com.chat_system.api_gateway.presentation.dto.request.friend_request.FriendRequestCreateUserRequest;
import com.chat_system.api_gateway.presentation.dto.request.friend_request.FriendRequestResponseRequest;

@RestController
@RequestMapping("/friend/request")
public class FriendRequestUserController {
    private final FriendRequestUserWebClient friendRequestUserWebClient;

    public FriendRequestUserController(FriendRequestUserWebClient friendRequestUserWebClient) {
        this.friendRequestUserWebClient = friendRequestUserWebClient;
    }
    
    // get user friend requests
        // token
        // specification
    @GetMapping("")
    public ResponseEntity<?> getFriendRequests(
        @RequestHeader String token,
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
            return ResponseEntity.ok().body(friendRequestUserWebClient.getFriendRequests(token, page, size, ascSort, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get user friend request by id
        // token
        // friend request id
    @GetMapping("/{id}")
    public ResponseEntity<?> getFriendRequestById(
        @RequestHeader String token,
        @PathVariable String id
    ) 
    {
        try {
            return ResponseEntity.ok().body(friendRequestUserWebClient.getById(token, id));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get user send friend request 
        // token 
        // specification
    @GetMapping("/send")
    public ResponseEntity<?> getSendFriendRequests(
        @RequestHeader String token,
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
            return ResponseEntity.ok().body(friendRequestUserWebClient.getSent(token, page, size, ascSort, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get user receive friend request
        // token
        // specification
    @GetMapping("/receive")
    public ResponseEntity<?> getReceiveFriendRequests(
        @RequestHeader String token,
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
            return ResponseEntity.ok().body(friendRequestUserWebClient.getReceived(token, page, size, ascSort, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get user friend request with user
        // token
        // user id
        // specification
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getFriendRequestsWithUser(
        @RequestHeader String token,
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
            return ResponseEntity.ok().body(friendRequestUserWebClient.getWithUser(token, userId, page, size, ascSort, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get user friend request to user
        // token 
        // receiver id
        // specification
    @GetMapping("/receiver/{receiverId}")
    public ResponseEntity<?> getFriendRequestFromRequesterToUser(
        @RequestHeader String token,
        @PathVariable String receiverId
    ) 
    {
        try {
            return ResponseEntity.ok().body(friendRequestUserWebClient.getFromRequesterToUser(token, receiverId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get user friend request from user
        // token
        // sender id
        // specification
    @GetMapping("/sender/{senderId}")
    public ResponseEntity<?> getFriendRequestFromUserToRequester(
        @RequestHeader String token,
        @PathVariable String senderId
    ) 
    {
        try {
            return ResponseEntity.ok().body(friendRequestUserWebClient.getFromUserToRequester(token, senderId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // post new friend request
        // token
        // receiver id
    @PostMapping("")
    public ResponseEntity<?> postFriendRequest(@RequestHeader String token, @RequestBody FriendRequestCreateUserRequest request) {
        try {
            return ResponseEntity.ok().body(friendRequestUserWebClient.create(token, request));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // response user friend request
        // token
        // friend request id
        // response
    @PatchMapping("/{id}/response")
    public ResponseEntity<?> patchFriendRequestStatus(@RequestHeader String token, @PathVariable String id, @RequestBody FriendRequestResponseRequest request) {
        try {
            return ResponseEntity.ok().body(friendRequestUserWebClient.respond(token, id, request));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // delete user friend request
        // token 
        // friend request id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFriendRequest(@RequestHeader String token, @PathVariable String id) 
    {
        try {
            friendRequestUserWebClient.delete(token, id);
            return ResponseEntity.ok().body("Friend request deleted successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
