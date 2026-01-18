package com.owl.chat_service.presentation.rest.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import com.owl.chat_service.application.service.user.chat.ControlChatUserServices;
import com.owl.chat_service.application.service.user.chat.GetChatUserServices;
import com.owl.chat_service.presentation.dto.ResourceData;
import com.owl.chat_service.presentation.dto.ChatUpdateNameRequest;
import com.owl.chat_service.presentation.dto.user.ChatUserRequest;

import java.time.Instant;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/chat")
public class ChatUserController {

    private final ControlChatUserServices controlChatUserServices;
    private final GetChatUserServices getChatUserServices;

    public ChatUserController(GetChatUserServices getChatUserServices, ControlChatUserServices controlChatUserServices) {
        this.getChatUserServices = getChatUserServices;
        this.controlChatUserServices = controlChatUserServices;}

    // get chats by member id
        // requester id
        // member id
        // keywords
        // page
        // size
        // ascSort
        // type
        // joinDateStart
        // joinDateEnd
    @GetMapping("/member/{memberId}")
    public ResponseEntity<?> getChatsByMemberId(
        @RequestHeader String requesterId, 
        @PathVariable String memberId,
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
            return ResponseEntity.ok().body(getChatUserServices.getChatsByMemberId(requesterId, memberId, keywords, page, size, ascSort, type, joinDateStart, joinDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }
    
    // get chat by chat id
        // requester id
        // member id
    @GetMapping("/{chatId}")
    public ResponseEntity<?> getChatByChatId(@RequestHeader String requesterId, @PathVariable String chatId) {
        try {
            return ResponseEntity.ok().body(getChatUserServices.getChatById(requesterId, chatId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get chat avatar
        // requester id
        // avatar
    @GetMapping("/{chatId}/avatar")
    public ResponseEntity<?> getChatAvatar(@RequestHeader String requesterId, @PathVariable String chatId) {
        try {
            ResourceData data = getChatUserServices.getChatAvatar(requesterId, chatId);

            String mediaType = data.contentType /* avatar mediaType */;
            
            Objects.requireNonNull(mediaType, "mediaType must not be null");

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mediaType))
                    .body(data.resource /* avatar resource */);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // post chat
        // chat create request
            // name
            // chat member id list
    @PostMapping("")
    public ResponseEntity<?> postChat(@RequestHeader String requesterId, @RequestBody ChatUserRequest chatRequest) {
        try {
            return ResponseEntity.ok().body(controlChatUserServices.addNewChat(requesterId, chatRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // patch chat name
        // requester id
        // chat id
        // chat name
    @PatchMapping("/{chatId}/name")
    public ResponseEntity<?> patchChatName(@RequestHeader String requesterId, @RequestHeader String chatId, @RequestBody ChatUpdateNameRequest name) {
        try {
            return ResponseEntity.ok().body(controlChatUserServices.updateChatName(requesterId, chatId, name.name));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // patch chat avatar
        // requester id
        // chat id
        // chat avatar
    @PostMapping(value = "/{chatId}/avatar/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> patchChatAvatar(@RequestHeader String requesterId, @PathVariable String chatId, @RequestPart("file") MultipartFile avatarFile) {
        try {
            controlChatUserServices.updateChatAvatarFile(requesterId, chatId, avatarFile);
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
    
    // deactivate chat
        // requester id
        // chat id
    @DeleteMapping("/{chatId}/deactivate")
    public ResponseEntity<String> deactivateChat(@RequestHeader String requesterId, @PathVariable String chatId) {
        try {
            controlChatUserServices.deleteChat(requesterId, chatId);
            return ResponseEntity.ok("Deactivate chat successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
