package com.owl.social_service.presentation.rest.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.owl.social_service.application.admin.ControlFriendshipAdminServices;
import com.owl.social_service.application.admin.GetFriendshipAdminServices;
import com.owl.social_service.presentation.dto.FriendshipCreateRequest;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/admin/friendship")
public class FriendshipAdminController {

    private final ControlFriendshipAdminServices controlFriendshipAdminServices;
    private final GetFriendshipAdminServices getFriendshipAdminServices;

    public FriendshipAdminController(GetFriendshipAdminServices getFriendshipAdminServices, ControlFriendshipAdminServices controlFriendshipAdminServices) {
        this.getFriendshipAdminServices = getFriendshipAdminServices;
        this.controlFriendshipAdminServices = controlFriendshipAdminServices;}

    @GetMapping("")
    public ResponseEntity<?> getFriendships(
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "true") boolean ascSort,
        @RequestParam(required = false) String keywords,
        @RequestParam(required = false) Instant createdDateStart,
        @RequestParam(required = false) Instant createdDateEnd
    ) 
    {
        try {
            return ResponseEntity.ok().body(getFriendshipAdminServices.getFriendships(page, size, ascSort, keywords, createdDateStart, createdDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<?>  getFriendshipsByUserId(
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
            return ResponseEntity.ok().body(getFriendshipAdminServices.getFriendshipsByUserId(userId, page, size, ascSort, keywords, createdDateStart, createdDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/first-user/{firstUserId}/second-user/{secondUserId}")
    public ResponseEntity<?>  getFriendshipsByUsersId(
        @PathVariable String firstUserId,
        @PathVariable String secondUserId
    ) 
    {
        try {
            return ResponseEntity.ok().body(getFriendshipAdminServices.getFriendshipByUsersId(firstUserId, secondUserId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?>  getFriendshipsById(
        @PathVariable String id
    ) 
    {
        try {
            return ResponseEntity.ok().body(getFriendshipAdminServices.getFriendshipById(id));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PostMapping("")
    public ResponseEntity<?>  postFriendship(@RequestBody FriendshipCreateRequest friendshipCreateRequest) 
    {
        try {
            return ResponseEntity.ok().body(controlFriendshipAdminServices.addNewFriendship(friendshipCreateRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?>  deleteFriendshipById(@PathVariable String id) 
    {
        try {
            controlFriendshipAdminServices.deleteFriendship(id);
            return ResponseEntity.ok().body("Friendship deleted successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
