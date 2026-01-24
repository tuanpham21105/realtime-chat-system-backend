package com.owl.social_service.presentation.rest.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

import com.owl.social_service.application.user.ControlFriendshipUserServices;
import com.owl.social_service.application.user.GetFriendshipUserServies;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/friendship")
public class FriendshipUserController {

    private final ControlFriendshipUserServices controlFriendshipUserServices;

    private final GetFriendshipUserServies getFriendshipUserServies;
    public FriendshipUserController(GetFriendshipUserServies getFriendshipUserServies, ControlFriendshipUserServices controlFriendshipUserServices) {
        this.getFriendshipUserServies = getFriendshipUserServies;
        this.controlFriendshipUserServices = controlFriendshipUserServices;
    }

    @GetMapping("")
    public ResponseEntity<?> getFriendships(
        @RequestHeader String requesterId,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "true") boolean ascSort,
        // @RequestParam(required = false) String keywords,
        @RequestParam(required = false) Instant createdDateStart,
        @RequestParam(required = false) Instant createdDateEnd
    ) 
    {
        try {
            return ResponseEntity.ok().body(getFriendshipUserServies.getFriendshipsOfUser(requesterId, page, size, ascSort, createdDateStart, createdDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFriendshipById(
        @RequestHeader String requesterId,
        @PathVariable String id
    ) 
    {
        try {
            return ResponseEntity.ok().body(getFriendshipUserServies.getFrienshipById(requesterId, id));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getFriendshipWithUser(
        @RequestHeader String requesterId,
        @PathVariable String userId
    ) 
    {
        try {
            return ResponseEntity.ok().body(getFriendshipUserServies.getFriendshipWithUser(requesterId, userId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFriendship(
        @RequestHeader String requesterId,
        @PathVariable String id
    ) 
    {
        try {
            controlFriendshipUserServices.deleteFriendship(requesterId, id);
            return ResponseEntity.ok().body("Friendship deleted successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
