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

import com.chat_system.api_gateway.external_service.client.BlockUserWebClient;
import com.chat_system.api_gateway.presentation.dto.request.block.BlockCreateUserRequest;

@RestController
@RequestMapping("/block")
public class BlockUserController {
    private final BlockUserWebClient blockUserWebClient;

    public BlockUserController(BlockUserWebClient blockUserWebClient) {
        this.blockUserWebClient = blockUserWebClient;
    }

    // get user blocked by id
        // token 
        // block id
    @GetMapping("/{id}")
    public ResponseEntity<?>  getBlockById(
        @RequestHeader String token,
        @PathVariable String id
    ) 
    {
        try {
            return ResponseEntity.ok().body(blockUserWebClient.getBlockById(token, id));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get user blocked 
        // token
        // specification
    @GetMapping("/blocked")
    public ResponseEntity<?> getUserBlocked(
        @RequestHeader String token,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "true") boolean ascSort,
        @RequestParam(required = false) Instant createdDateStart,
        @RequestParam(required = false) Instant createdDateEnd
    ) 
    {
        try {
            return ResponseEntity.ok().body(blockUserWebClient.getUserBlocked(token, page, size, ascSort, createdDateStart, createdDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // post new block
        // token
        // blocked id
    @PostMapping("")
    public ResponseEntity<?> postBlock(@RequestHeader String token, @RequestBody BlockCreateUserRequest request) {
        try {
            return ResponseEntity.ok().body(blockUserWebClient.createBlock(token, request));
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
            blockUserWebClient.deleteBlock(token, id);
            return ResponseEntity.ok().body("Block deleted successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
