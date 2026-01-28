package com.chat_system.api_gateway.presentation.rest.chat;

import java.time.Instant;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.chat_system.api_gateway.external_service.client.ChatAdminWebClient;
import com.chat_system.api_gateway.presentation.dto.request.chat.ChatAdminRequest;

@RestController
@RequestMapping("/admin/chat")
public class ChatAdminController {
    private final ChatAdminWebClient chatAdminWebClient;

    public ChatAdminController(ChatAdminWebClient chatAdminWebClient) {
        this.chatAdminWebClient = chatAdminWebClient;
    }
    
    // get chats
        // token
        // specification
    @GetMapping("")
    public ResponseEntity<?> getChats(
        @RequestHeader String token,
        @RequestParam(required = false) String keywords,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "true") boolean ascSort,
        @RequestParam(required = false) Boolean status,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String initiatorId,
        @RequestParam(required = false) Instant createdDateStart,
        @RequestParam(required = false) Instant createdDateEnd
    ) 
    {
        try {
            return ResponseEntity.ok().body(chatAdminWebClient.getChats(keywords, page, size, ascSort, status, type, initiatorId, createdDateStart, createdDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get chat by id
        // token
        // chat id
    @GetMapping("/{chatId}")
    public ResponseEntity<?> getChatById(@RequestHeader String token, @PathVariable String chatId) {
        try {
            return ResponseEntity.ok().body(chatAdminWebClient.getChatById(chatId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get chat avatar
        // token
        // chat id
    @GetMapping("/{chatId}/avatar")
    public ResponseEntity<?> getChatAvatar(@RequestHeader String token, @PathVariable String chatId) {
        try {
            ResponseEntity<Resource> data = chatAdminWebClient.getChatAvatarWithType(chatId);

            return ResponseEntity.ok()
                    .headers(data.getHeaders())
                    .body(data.getBody() /* avatar resource */);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // post new chat
        // token
        // chat type
        // chat name
        // initiator id
    @PostMapping("")
    public ResponseEntity<?> postChat(@RequestHeader String token, @RequestBody ChatAdminRequest chatRequest) {
        try {
            return ResponseEntity.ok().body(chatAdminWebClient.createChat(chatRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // update chat 
        // token
        // update chat
    @PutMapping("/{chatId}")
    public ResponseEntity<?> putChat(@RequestHeader String token, @PathVariable String chatId, @RequestBody ChatAdminRequest chatRequest) {
        try {
            return ResponseEntity.ok().body(chatAdminWebClient.updateChat(chatId, chatRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // patch chat status
        // chat id
        // chat status
    @PatchMapping("/{chatId}/status")
    public ResponseEntity<?> patchChatStatus(@RequestHeader String token, @PathVariable String chatId, @RequestBody boolean status) {
        try {
            return ResponseEntity.ok().body(chatAdminWebClient.updateChatStatus(chatId, status));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // update chat avatar
        // token
        // chat id
        // avatar file
    @PostMapping(value = "/{chatId}/avatar/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> patchChatAvatar(@RequestHeader String token, @PathVariable String chatId, @RequestPart("file") MultipartFile avatarFile) {
        try {
            chatAdminWebClient.uploadChatAvatar(chatId, avatarFile);
            return ResponseEntity.ok("Upload avatar successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSize(MaxUploadSizeExceededException e) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File size exceeds 10MB limit");
    }

    // delete chat
        // token
        // chat id
    @DeleteMapping("/{chatId}")
    public ResponseEntity<?> deleteChat(@RequestHeader String token, @PathVariable String chatId) {
        try {
            chatAdminWebClient.deleteChat(chatId);
            return ResponseEntity.ok().body("Chat deleted successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
