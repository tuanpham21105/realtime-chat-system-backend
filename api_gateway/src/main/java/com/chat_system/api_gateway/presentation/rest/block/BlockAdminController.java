package com.chat_system.api_gateway.presentation.rest.block;

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

import com.chat_system.api_gateway.external_service.client.BlockAdminWebClient;
import com.chat_system.api_gateway.presentation.dto.request.block.BlockCreateRequest;

@RestController
@RequestMapping("/admin/block")
public class BlockAdminController {
    private final BlockAdminWebClient blockAdminWebClient;

    public BlockAdminController(BlockAdminWebClient blockAdminWebClient) {
        this.blockAdminWebClient = blockAdminWebClient;
    }

    // get blocks
        // token
        // specification
    @GetMapping("")
    public ResponseEntity<?> getBlocks(
        @RequestHeader String token,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "true") boolean ascSort,
        @RequestParam(required = false) Instant createdDateStart,
        @RequestParam(required = false) Instant createdDateEnd
    ) 
    {
        try {
            return ResponseEntity.ok().body(blockAdminWebClient.getBlocks(page, size, ascSort, createdDateStart, createdDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get block by id
        // token
        // block id
    @GetMapping("/{id}")
    public ResponseEntity<?>  getBlockById(
        @RequestHeader String token,
        @PathVariable String id
    ) 
    {
        try {
            return ResponseEntity.ok().body(blockAdminWebClient.getBlockById(id));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get user blocked
        // token
        // user id
        // specification
    @GetMapping("/user/{userId}/blocked")
    public ResponseEntity<?> getUserBlocked(
        @RequestHeader String token,
        @PathVariable String userId,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "true") boolean ascSort,
        @RequestParam(required = false) Instant createdDateStart,
        @RequestParam(required = false) Instant createdDateEnd
    ) 
    {
        try {
            return ResponseEntity.ok().body(blockAdminWebClient.getUserBlocked(userId, page, size, ascSort, createdDateStart, createdDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get user blocker
        // token
        // user id
        // specification
    @GetMapping("/user/{userId}/blocker")
    public ResponseEntity<?> getUserBlocker(
        @RequestHeader String token,
        @PathVariable String userId,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "true") boolean ascSort,
        @RequestParam(required = false) Instant createdDateStart,
        @RequestParam(required = false) Instant createdDateEnd
    ) 
    {
        try {
            return ResponseEntity.ok().body(blockAdminWebClient.getUserBlocker(userId, page, size, ascSort, createdDateStart, createdDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get user block user
        // token
        // blocker id
        // blocked id
    @GetMapping("/blocker/{blockerId}/blocked/{blockedId}")
    public ResponseEntity<?> getUserBlockUser(
        @RequestHeader String token,
        @PathVariable String blockerId,
        @PathVariable String blockedId
    ) 
    {
        try {
            return ResponseEntity.ok().body(blockAdminWebClient.getUserBlockUser(blockerId, blockedId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // post new block
        // token
        // blocker id
        // blocked id
    @PostMapping("")
    public ResponseEntity<?> postBlock(@RequestHeader String token, @RequestBody BlockCreateRequest request) {
        try {
            return ResponseEntity.ok().body(blockAdminWebClient.createBlock(request));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // delete block
        // token
        // block id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlock(
        @RequestHeader String token,
        @PathVariable String id
    ) 
    {
        try {
            blockAdminWebClient.deleteBlock(id);
            return ResponseEntity.ok().body("Block deleted successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
