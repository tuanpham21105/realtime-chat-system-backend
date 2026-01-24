package com.owl.social_service.presentation.rest.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

import com.owl.social_service.application.admin.ControlBlockAdminServices;
import com.owl.social_service.application.admin.GetBlockAdminServices;
import com.owl.social_service.presentation.dto.BlockCreateRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/admin/block")
public class BlockAdminController {

    private final ControlBlockAdminServices controlBlockAdminServices;

    private final GetBlockAdminServices getBlockAdminServices;
    public BlockAdminController(GetBlockAdminServices getBlockAdminServices, ControlBlockAdminServices controlBlockAdminServices) {
        this.getBlockAdminServices = getBlockAdminServices;
        this.controlBlockAdminServices = controlBlockAdminServices;
    }

    @GetMapping("")
    public ResponseEntity<?> getBlocks(
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "true") boolean ascSort,
        @RequestParam(required = false) Instant createdDateStart,
        @RequestParam(required = false) Instant createdDateEnd
    ) 
    {
        try {
            return ResponseEntity.ok().body(getBlockAdminServices.getBlocks(page, size, ascSort, createdDateStart, createdDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?>  getBlockById(
        @PathVariable String id
    ) 
    {
        try {
            return ResponseEntity.ok().body(getBlockAdminServices.getBlockById(id));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}/blocked")
    public ResponseEntity<?> getUserBlocked(
        @PathVariable String userId,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "true") boolean ascSort,
        @RequestParam(required = false) Instant createdDateStart,
        @RequestParam(required = false) Instant createdDateEnd
    ) 
    {
        try {
            return ResponseEntity.ok().body(getBlockAdminServices.getUserBlocked(userId, page, size, ascSort, createdDateStart, createdDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}/blocker")
    public ResponseEntity<?> getUserBlocker(
        @PathVariable String userId,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "true") boolean ascSort,
        @RequestParam(required = false) Instant createdDateStart,
        @RequestParam(required = false) Instant createdDateEnd
    ) 
    {
        try {
            return ResponseEntity.ok().body(getBlockAdminServices.getUserBlocker(userId, page, size, ascSort, createdDateStart, createdDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/blocker/{blockerId}/blocked/{blockedId}")
    public ResponseEntity<?> getUserBlockUser(
        @PathVariable String blockerId,
        @PathVariable String blockedId
    ) 
    {
        try {
            return ResponseEntity.ok().body(getBlockAdminServices.getUserBlockUser(blockerId, blockedId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("")
    public ResponseEntity<?> postBlock(@RequestBody BlockCreateRequest request) {
        try {
            return ResponseEntity.ok().body(controlBlockAdminServices.addNewBlock(request));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlock(
        @PathVariable String id
    ) 
    {
        try {
            controlBlockAdminServices.deleteBlock(id);
            return ResponseEntity.ok().body("Block deleted successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
