package com.chat_system.api_gateway.presentation.rest.friendship;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chat_system.api_gateway.external_service.client.FriendshipUserWebClient;

@RestController
@RequestMapping("/friend")
public class FriendshipUserController {
    private final FriendshipUserWebClient friendshipUserWebClient;
    public FriendshipUserController(FriendshipUserWebClient friendshipUserWebClient) {
        this.friendshipUserWebClient = friendshipUserWebClient;
    }

    // get user friendships
        // token
        // specification
    @GetMapping("")
    public ResponseEntity<?> getFriendships(
        @RequestHeader String token,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "true") boolean ascSort,
        // @RequestParam(required = false) String keywords,
        @RequestParam(required = false) Instant createdDateStart,
        @RequestParam(required = false) Instant createdDateEnd
    ) 
    {
        try {
            return ResponseEntity.ok().body(friendshipUserWebClient.getFriendships(token, page, size, ascSort, createdDateStart, createdDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get user friendship by id
        // token 
        // friendship id
    @GetMapping("/{id}")
    public ResponseEntity<?> getFriendshipById(
        @RequestHeader String token,
        @PathVariable String id
    ) 
    {
        try {
            return ResponseEntity.ok().body(friendshipUserWebClient.getFriendshipById(token, id));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get friendship with user
        // token
        // user id
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getFriendshipWithUser(
        @RequestHeader String token,
        @PathVariable String userId
    ) 
    {
        try {
            return ResponseEntity.ok().body(friendshipUserWebClient.getFriendshipWithUser(token, userId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // delete friendship
        // token
        // friendship id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFriendship(
        @RequestHeader String token,
        @PathVariable String id
    ) 
    {
        try {
            friendshipUserWebClient.deleteFriendship(token, id);
            return ResponseEntity.ok().body("Friendship deleted successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
