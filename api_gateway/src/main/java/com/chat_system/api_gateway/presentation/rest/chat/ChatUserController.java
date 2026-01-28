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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.chat_system.api_gateway.external_service.client.ChatUserWebClient;
import com.chat_system.api_gateway.presentation.dto.request.chat.ChatUpdateNameRequest;
import com.chat_system.api_gateway.presentation.dto.request.chat.ChatUserRequest;

@RestController
@RequestMapping("/chat")
public class ChatUserController {
    private final ChatUserWebClient chatUserWebClient;

    public ChatUserController(ChatUserWebClient chatUserWebClient) {
        this.chatUserWebClient = chatUserWebClient;
    }

    // get user chats
        // token
        // specification
    @GetMapping("/member")
    public ResponseEntity<?> getChatsByMemberId(
        @RequestHeader String requesterId, 
        @RequestParam(required = false, defaultValue = "") String keywords,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "false") boolean ascSort,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) Instant joinDateStart,
        @RequestParam(required = false) Instant joinDateEnd
    ) 
    {
        try {
            return ResponseEntity.ok().body(chatUserWebClient.getChatsByMember(requesterId, keywords, page, size, ascSort, type, joinDateStart, joinDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get user chat by id
        // token
        // chat id
    @GetMapping("/{chatId}")
    public ResponseEntity<?> getChatByChatId(@RequestHeader String requesterId, @PathVariable String chatId) {
        try {
            return ResponseEntity.ok().body(chatUserWebClient.getChatById(requesterId, chatId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get user chat avatar
        // token
        // chat id
    @GetMapping("/{chatId}/avatar")
    public ResponseEntity<?> getChatAvatar(@RequestHeader String requesterId, @PathVariable String chatId) {
        try {
            ResponseEntity<Resource> data = chatUserWebClient.getChatAvatarFull(requesterId, chatId);

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
        // chat name
        // chat member id list
    @PostMapping("")
    public ResponseEntity<?> postChat(@RequestHeader String requesterId, @RequestBody ChatUserRequest chatRequest) {
        try {
            return ResponseEntity.ok().body(chatUserWebClient.createChat(requesterId, chatRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // update user chat name
        // token
        // chat id
        // chat name
    @PatchMapping("/{chatId}/name")
    public ResponseEntity<?> patchChatName(@RequestHeader String requesterId, @PathVariable String chatId, @RequestBody ChatUpdateNameRequest name) {
        try {
            return ResponseEntity.ok().body(chatUserWebClient.updateChatName(requesterId, chatId, name));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // update user chat avatar
        // token
        // chat id
        // avatar file
    @PostMapping(value = "/{chatId}/avatar/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> patchChatAvatar(@RequestHeader String requesterId, @PathVariable String chatId, @RequestPart("file") MultipartFile avatarFile) {
        try {
            chatUserWebClient.uploadChatAvatar(requesterId, chatId, avatarFile);
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

    // delete user chat
        // token
        // chat id
    @DeleteMapping("/{chatId}/delete")
    public ResponseEntity<String> deactivateChat(@RequestHeader String requesterId, @PathVariable String chatId) {
        try {
            chatUserWebClient.deactivateChat(requesterId, chatId);
            return ResponseEntity.ok("Delete user chat successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
