package com.owl.social_service.presentation.rest.user;

import java.time.Instant;

import com.owl.social_service.application.user.ControlBlockUserServices;
import com.owl.social_service.application.user.GetBlockUserServices;
import com.owl.social_service.presentation.dto.BlockCreateUserRequest;

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

@RestController
@RequestMapping("/block")
public class BlockUserController {

    private final ControlBlockUserServices controlBlockUserServices;

    private final GetBlockUserServices getBlockUserServices;
    public BlockUserController(GetBlockUserServices getBlockUserServices, ControlBlockUserServices controlBlockUserServices) {
        this.getBlockUserServices = getBlockUserServices;
        this.controlBlockUserServices = controlBlockUserServices;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?>  getBlockById(
        @RequestHeader String requesterId,
        @PathVariable String id
    ) 
    {
        try {
            return ResponseEntity.ok().body(getBlockUserServices.getBlockById(requesterId, id));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/blocked")
    public ResponseEntity<?> getUserBlocked(
        @RequestHeader String requesterId,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "true") boolean ascSort,
        @RequestParam(required = false) Instant createdDateStart,
        @RequestParam(required = false) Instant createdDateEnd
    ) 
    {
        try {
            return ResponseEntity.ok().body(getBlockUserServices.getUserBlocked(requesterId, page, size, ascSort, createdDateStart, createdDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("")
    public ResponseEntity<?> postBlock(@RequestHeader String requesterId, @RequestBody BlockCreateUserRequest request) {
        try {
            return ResponseEntity.ok().body(controlBlockUserServices.addNewBlock(requesterId, request.blockedId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlock(
        @RequestHeader String requesterId,
        @PathVariable String id
    ) 
    {
        try {
            controlBlockUserServices.deleteBlock(requesterId, id);
            return ResponseEntity.ok().body("Block deleted successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
