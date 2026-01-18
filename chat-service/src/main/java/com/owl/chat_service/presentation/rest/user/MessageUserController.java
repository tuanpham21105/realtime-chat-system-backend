package com.owl.chat_service.presentation.rest.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.owl.chat_service.application.service.user.message.ControlMessageUserServices;
import com.owl.chat_service.application.service.user.message.GetMessageUserServices;
import com.owl.chat_service.presentation.dto.ResourceData;
import com.owl.chat_service.presentation.dto.FileMessageUserRequest;
import com.owl.chat_service.presentation.dto.MessageUpdateContentRequest;
import com.owl.chat_service.presentation.dto.TextMessageUserRequest;
import com.owl.chat_service.presentation.dto.admin.FileMessageAdminRequest;

import java.time.Instant;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/message")
public class MessageUserController {

    private final ControlMessageUserServices controlMessageUserServices;
    private final GetMessageUserServices getMessageUserServices;

    public MessageUserController(GetMessageUserServices getMessageUserServices, ControlMessageUserServices controlMessageUserServices) {
        this.getMessageUserServices = getMessageUserServices;
        this.controlMessageUserServices = controlMessageUserServices;
    }

    // get messages by chat id
        // requester id
        // chat id
        // keywords
        // page
        // size
        // ascSort
        // type
        // sender id
        // sentDateStart
        // sentDateEnd
    @GetMapping("/chat/{chatId}")
    public ResponseEntity<?>  getMessagesByChatId(
        @RequestHeader String requesterId,
        @PathVariable String chatId,
        @RequestParam(required = false, defaultValue = "") String keywords,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "true") boolean ascSort,
        @RequestParam(required = false, defaultValue = "ALL") String type,
        @RequestParam(required = false, defaultValue = "") String senderId,
        @RequestParam(required = false) Instant sentDateStart,
        @RequestParam(required = false) Instant sentDateEnd 
    ) 
    {
        try {
            return ResponseEntity.ok().body(getMessageUserServices.getMessagesByChatId(requesterId, chatId, keywords, page, size, ascSort, type, senderId, sentDateStart, sentDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    

    // get message by id
        // requester id
        // message id
    @GetMapping("/{messageId}")
    public ResponseEntity<?> getMessageById(@RequestHeader String requesterId, @PathVariable String messageId) {
        try {
            return ResponseEntity.ok().body(getMessageUserServices.getMessageById(requesterId, messageId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // get message file by id
        // requester id
        // message id
    @GetMapping("/{messageId}/resource")
    public ResponseEntity<?> getMessageFile(@RequestHeader String requesterId, @PathVariable String messageId) {
        try {
            ResourceData data = getMessageUserServices.getMessageFile(requesterId, messageId);

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

    // post new text message
        // requester id
        // message create request
            // chat id
            // content
    @PostMapping("")
    public ResponseEntity<?> postNewTextMessage(@RequestHeader String requesterId, @RequestBody TextMessageUserRequest textMessageRequest) {
        try {
            return ResponseEntity.ok().body(controlMessageUserServices.addNewTextMessage(requesterId, textMessageRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // post new file message
        // requester id
        // chat id
        // type
        // file
    @PostMapping(value = "/resource/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> postNewFileMessage(@RequestHeader String requesterId,  @RequestHeader String chatId, @RequestHeader String type, @RequestPart("file") MultipartFile file) {
        try {
            FileMessageUserRequest fileMessageRequest = new FileMessageAdminRequest();
            fileMessageRequest.chatId = chatId;
            fileMessageRequest.type = type;
            fileMessageRequest.file = file;
            return ResponseEntity.ok().body(controlMessageUserServices.addNewFileMessage(requesterId, fileMessageRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSize(MaxUploadSizeExceededException e) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File size exceeds 10MB limit");
    }
    
    // put edit text message
        // requester id
        // message id
        // content
    @PutMapping("/{messageId}/edit")
    public ResponseEntity<?> putTextMessage(@RequestHeader String requesterId, @PathVariable String messageId, @RequestBody MessageUpdateContentRequest content) {
        try {
            return ResponseEntity.ok(controlMessageUserServices.editTextMessage(requesterId, messageId, content.content));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // soft delete message
        // requester id
        // message id
    @DeleteMapping("/{messageId}")
    public ResponseEntity<?> softDeleteMessage(@RequestHeader String requesterId, @PathVariable String messageId) {
        try {
            controlMessageUserServices.softDeleteMessage(requesterId, messageId);
            return ResponseEntity.ok().body("Message removed successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
