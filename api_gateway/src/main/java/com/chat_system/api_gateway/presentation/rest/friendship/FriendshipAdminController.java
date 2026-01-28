package com.chat_system.api_gateway.presentation.rest.friendship;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chat_system.api_gateway.external_service.client.FriendshipAdminWebClient;
import com.chat_system.api_gateway.presentation.dto.request.friendship.FriendshipCreateRequest;

@RestController
@RequestMapping("/admin/friend")
public class FriendshipAdminController {
    private final FriendshipAdminWebClient friendshipAdminWebClient;

    public FriendshipAdminController(FriendshipAdminWebClient friendshipAdminWebClient) {
        this.friendshipAdminWebClient = friendshipAdminWebClient;
    }

    // get friendships
        // token 
        // specification
    @GetMapping("")
    public ResponseEntity<?> getFriendships(
        @RequestHeader String token,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "true") boolean ascSort,
        @RequestParam(required = false) String keywords,
        @RequestParam(required = false) Instant createdDateStart,
        @RequestParam(required = false) Instant createdDateEnd
    ) 
    {
        try {
            return ResponseEntity.ok().body(friendshipAdminWebClient.getFriendships(page, size, ascSort, keywords, createdDateStart, createdDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // get friendship by id
        // token
        // friendship id
    @GetMapping("/user/{userId}")
    public ResponseEntity<?>  getFriendshipsByUserId(
        @RequestHeader String token,
        @PathVariable String userId,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "true") boolean ascSort,
        @RequestParam(required = false) String keywords,
        @RequestParam(required = false) Instant createdDateStart,
        @RequestParam(required = false) Instant createdDateEnd
    ) 
    {
        try {
            return ResponseEntity.ok().body(friendshipAdminWebClient.getFriendshipsByUserId(userId, page, size, ascSort, keywords, createdDateStart, createdDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // get friendships by user id
        // token
        // user id
        // specification
    @GetMapping("/first-user/{firstUserId}/second-user/{secondUserId}")
    public ResponseEntity<?>  getFriendshipsByUsersId(
        @RequestHeader String token,
        @PathVariable String firstUserId,
        @PathVariable String secondUserId
    ) 
    {
        try {
            return ResponseEntity.ok().body(friendshipAdminWebClient.getFriendshipByUsersId(firstUserId, secondUserId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // get friendships by users id 
        // token
        // first user id
        // second user id 
    @GetMapping("/{id}")
    public ResponseEntity<?>  getFriendshipsById(
        @RequestHeader String token,
        @PathVariable String id
    ) 
    {
        try {
            return ResponseEntity.ok().body(friendshipAdminWebClient.getFriendshipById(id));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // post new friendship
        //token
        // first user id
        // second user id
    @PostMapping("")
    public ResponseEntity<?>  postFriendship(@RequestHeader String token, @RequestBody FriendshipCreateRequest friendshipCreateRequest) 
    {
        try {
            return ResponseEntity.ok().body(friendshipAdminWebClient.createFriendship(friendshipCreateRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // delete friendship
        // token 
        // friendship id
    @DeleteMapping("/{id}")
    public ResponseEntity<?>  deleteFriendshipById(@RequestHeader String token, @PathVariable String id) 
    {
        try {
            friendshipAdminWebClient.deleteFriendship(id);
            return ResponseEntity.ok().body("Friendship deleted successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
