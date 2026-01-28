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

import com.chat_system.api_gateway.external_service.client.FriendRequestAdminWebClient;
import com.chat_system.api_gateway.presentation.dto.request.friend_request.FriendRequestCreateRequest;
import com.chat_system.api_gateway.presentation.dto.request.friend_request.FriendRequestResponseRequest;

@RestController
@RequestMapping("/admin/friend/request")
public class FriendRequestAdminController {
    private final FriendRequestAdminWebClient friendRequestAdminWebClient;

    public FriendRequestAdminController(FriendRequestAdminWebClient friendRequestAdminWebClient) {
        this.friendRequestAdminWebClient = friendRequestAdminWebClient;
    }

    // get friend requests 
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
            return ResponseEntity.ok().body(friendRequestAdminWebClient.getFriendRequests(page, size, ascSort, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get friend request by id 
        // token
        // friend request id
    @GetMapping("/{id}")
    public ResponseEntity<?> getFriendRequestById(@RequestHeader String token, @PathVariable String id) 
    {
        try {
            return ResponseEntity.ok().body(friendRequestAdminWebClient.getFriendRequestById(id));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get friend request by sender id
        // token
        // sender id
        // specification
    @GetMapping("/sender/{senderId}")
    public ResponseEntity<?> getFriendRequestsBySenderId(
        @RequestHeader String token,
        @PathVariable String senderId,
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
            return ResponseEntity.ok().body(friendRequestAdminWebClient.getBySenderId(senderId, page, size, ascSort, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get friend request by receiver id
        // token
        // receiver id
        // specification
    @GetMapping("/receiver/{receiverId}")
    public ResponseEntity<?> getFriendRequestsByReceiverId(
        @RequestHeader String token,
        @PathVariable String receiverId,
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
            return ResponseEntity.ok().body(friendRequestAdminWebClient.getByReceiverId(receiverId, page, size, ascSort, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get friend request by user id
        // token 
        // user id
        // specification
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getFriendRequestsByUserId(
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
            return ResponseEntity.ok().body(friendRequestAdminWebClient.getByUserId(userId, page, size, ascSort, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get friend request from user to user
    @GetMapping("/sender/{senderId}/receiver/{receiverId}")
    public ResponseEntity<?> getFriendRequestFromUserToUser(
        @RequestHeader String token,
        @PathVariable String senderId,
        @PathVariable String receiverId
    ) 
    {
        try {
            return ResponseEntity.ok().body(friendRequestAdminWebClient.getFromUserToUser(senderId, receiverId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get friend request between users
    @GetMapping("/first-user/{firstUserId}/second-user/{secondUserId}")
    public ResponseEntity<?> getFriendRequestsByUsersId(
        @RequestHeader String token,
        @PathVariable String firstUserId,
        @PathVariable String secondUserId,
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
            return ResponseEntity.ok().body(friendRequestAdminWebClient.getByUsers(firstUserId, secondUserId, page, size, ascSort, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // post new friend request
        // token
        // sender id
        // receiver id
    @PostMapping("")
    public ResponseEntity<?> postFriendRequest(@RequestHeader String token, @RequestBody FriendRequestCreateRequest request) {
        try {
            return ResponseEntity.ok().body(friendRequestAdminWebClient.createFriendRequest(request));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // response friend request
        // token
        // friend request id
        // response 
    @PatchMapping("/{id}/response")
    public ResponseEntity<?> patchFriendRequestStatus(@RequestHeader String token, @PathVariable String id, @RequestBody FriendRequestResponseRequest request) {
        try {
            return ResponseEntity.ok().body(friendRequestAdminWebClient.updateStatus(id, request));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // delete friend request
        // token
        // friend request id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFriendRequest(@RequestHeader String token, @PathVariable String id) 
    {
        try {
            friendRequestAdminWebClient.deleteFriendRequest(id);
            return ResponseEntity.ok().body("Friend request deleted successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
