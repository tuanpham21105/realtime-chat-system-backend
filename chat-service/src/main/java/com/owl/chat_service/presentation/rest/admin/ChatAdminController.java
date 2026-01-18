package com.owl.chat_service.presentation.rest.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import com.owl.chat_service.application.service.admin.chat.ControlChatAdminServices;
import com.owl.chat_service.application.service.admin.chat.GetChatAdminServices;
import com.owl.chat_service.presentation.dto.ResourceData;
import com.owl.chat_service.presentation.dto.admin.ChatAdminRequest;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/admin/chat")
public class ChatAdminController {

    private final ControlChatAdminServices controlChatAdminService;
    private final GetChatAdminServices getChatAdminServices;

    public ChatAdminController(GetChatAdminServices getChatAdminServices, ControlChatAdminServices controlChatAdminService) {
        this.getChatAdminServices = getChatAdminServices;
        this.controlChatAdminService = controlChatAdminService;
    }

    // get chat
        // keywords
        // page
        // size
        // ascSort
        // type
        // createdDateStart
        // createdDateEnd
    @GetMapping("")
    public ResponseEntity<?> getChats(
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
            return ResponseEntity.ok().body(getChatAdminServices.getChats(keywords, page, size, ascSort, status, type, initiatorId, createdDateStart, createdDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    

    // get chat by id
        // chat id
    @GetMapping("/{chatId}")
    public ResponseEntity<?> getChatById(@PathVariable String chatId) {
        try {
            return ResponseEntity.ok().body(getChatAdminServices.getChatById(chatId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get chat avatar
        // chat id
    @GetMapping("/{chatId}/avatar")
    public ResponseEntity<?> getChatAvatar(@PathVariable String chatId) {
        try {
            ResourceData data = getChatAdminServices.getChatAvatarFile(chatId);

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
    @PostMapping("")
    public ResponseEntity<?> postChat(@RequestBody ChatAdminRequest chatRequest) {
        try {
            return ResponseEntity.ok().body(controlChatAdminService.addNewChat(chatRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // put chat
        // chat id
        // chat update request
    @PutMapping("/{chatId}")
    public ResponseEntity<?> putChat(@PathVariable String chatId, @RequestBody ChatAdminRequest chatRequest) {
        try {
            return ResponseEntity.ok().body(controlChatAdminService.updateChat(chatId, chatRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // patch chat status
        // chat id
        // chat status
    @PatchMapping("/{chatId}/status")
    public ResponseEntity<?> patchChatStatus(@PathVariable String chatId, @RequestBody boolean status) {
        try {
            return ResponseEntity.ok().body(controlChatAdminService.updateChatStatus(chatId, status));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // patch chat avatar
        // chat id
        // chat avatar
    @PostMapping(value = "/{chatId}/avatar/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> patchChatAvatar(@PathVariable String chatId, @RequestPart("file") MultipartFile avatarFile) {
        try {
            controlChatAdminService.updateChatAvatarFile(chatId, avatarFile);
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
        // chat id
    @DeleteMapping("/{chatId}")
    public ResponseEntity<?> deleteChat(@PathVariable String chatId) {
        try {
            controlChatAdminService.deleteChat(chatId);
            return ResponseEntity.ok().body("Chat deleted successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
