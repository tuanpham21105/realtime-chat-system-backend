package com.owl.social_service.presentation.rest.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.owl.social_service.application.admin.ControlFriendRequestAdminServices;
import com.owl.social_service.application.admin.GetFriendRequestAdminServices;
import com.owl.social_service.presentation.dto.FriendRequestCreateRequest;
import com.owl.social_service.presentation.dto.FriendRequestResponseRequest;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/admin/friend-request")
public class FriendRequestAdminController {

    private final ControlFriendRequestAdminServices controlFriendRequestAdminServices;

    private final GetFriendRequestAdminServices getFriendRequestAdminServices;
    public FriendRequestAdminController(GetFriendRequestAdminServices getFriendRequestAdminServices, ControlFriendRequestAdminServices controlFriendRequestAdminServices) {
        this.getFriendRequestAdminServices = getFriendRequestAdminServices;
        this.controlFriendRequestAdminServices = controlFriendRequestAdminServices;
    }

    @GetMapping("")
    public ResponseEntity<?> getFriendRequests(
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
            return ResponseEntity.ok().body(getFriendRequestAdminServices.getFriendRequests(page, size, ascSort, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getFriendRequestById(@PathVariable String id) 
    {
        try {
            return ResponseEntity.ok().body(getFriendRequestAdminServices.getFriendRequestById(id));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/sender/{senderId}")
    public ResponseEntity<?> getFriendRequestsBySenderId(
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
            return ResponseEntity.ok().body(getFriendRequestAdminServices.getFriendRequestsBySenderId(senderId, page, size, ascSort, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/receiver/{receiverId}")
    public ResponseEntity<?> getFriendRequestsByReceiverId(
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
            return ResponseEntity.ok().body(getFriendRequestAdminServices.getFriendRequestsByReceiverId(receiverId, page, size, ascSort, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getFriendRequestsByUserId(
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
            return ResponseEntity.ok().body(getFriendRequestAdminServices.getFriendRequestsByUserId(userId, page, size, ascSort, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/first-user/{firstUserId}/second-user/{secondUserId}")
    public ResponseEntity<?> getFriendRequestsByUsersId(
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
            return ResponseEntity.ok().body(getFriendRequestAdminServices.getFriendRequestsByUsersId(firstUserId, secondUserId, page, size, ascSort, keywords, status, createdDateStart, createdDateEnd, updatedDateStart, updatedDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/sender/{senderId}/receiver/{receiverId}")
    public ResponseEntity<?> getFriendRequestFromUserToUser(
        @PathVariable String senderId,
        @PathVariable String receiverId
    ) 
    {
        try {
            return ResponseEntity.ok().body(getFriendRequestAdminServices.getFriendRequestFromUserToUser(senderId, receiverId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("")
    public ResponseEntity<?> postFriendRequest(@RequestBody FriendRequestCreateRequest request) {
        try {
            return ResponseEntity.ok().body(controlFriendRequestAdminServices.addNewFriendRequest(request));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/response")
    public ResponseEntity<?> patchFriendRequestStatus(@PathVariable String id, @RequestBody FriendRequestResponseRequest request) {
        try {
            return ResponseEntity.ok().body(controlFriendRequestAdminServices.updateFriendRequestStatus(id, request.response));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFriendRequest(@PathVariable String id) 
    {
        try {
            controlFriendRequestAdminServices.deleteFriendRequest(id);
            return ResponseEntity.ok().body("Friend request deleted successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
