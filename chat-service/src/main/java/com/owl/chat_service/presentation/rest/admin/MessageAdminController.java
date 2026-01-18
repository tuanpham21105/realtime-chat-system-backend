package com.owl.chat_service.presentation.rest.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.owl.chat_service.application.service.admin.message.ControlMessageAdminServices;
import com.owl.chat_service.application.service.admin.message.GetMessageAdminServices;
import com.owl.chat_service.presentation.dto.ResourceData;
import com.owl.chat_service.presentation.dto.MessageUpdateContentRequest;
import com.owl.chat_service.presentation.dto.admin.FileMessageAdminRequest;
import com.owl.chat_service.presentation.dto.admin.TextMessageAdminRequest;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;


@RestController
@RequestMapping("/admin/message")
public class MessageAdminController {
    private final GetMessageAdminServices getMessageAdminServices;
    private final ControlMessageAdminServices controlMessageAdminServices;

    public MessageAdminController(GetMessageAdminServices getMessageAdminServices, ControlMessageAdminServices controlMessageAdminServices) {
        this.getMessageAdminServices = getMessageAdminServices;
        this.controlMessageAdminServices = controlMessageAdminServices;
    }
    
    // get messages
        // keywords
        // page
        // size
        // ascSort
        // status
        // state
        // type
        // sentDateStart
        // sentDateEnd
        // removedDateStart
        // removedDateEnd
        // createdDateStart
        // createdDateEnd
    @GetMapping("")
    public ResponseEntity<?> getMessages(
        @RequestParam(required = false, defaultValue = "") String keywords,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "true") boolean ascSort,
        @RequestParam(required = false) Boolean status,
        @RequestParam(required = false) String state,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) Instant sentDateStart,
        @RequestParam(required = false) Instant sentDateEnd,
        @RequestParam(required = false) Instant removedDateStart,
        @RequestParam(required = false) Instant removedDateEnd,
        @RequestParam(required = false) Instant createdDateStart,
        @RequestParam(required = false) Instant createdDateEnd
    ) 
    {
        try {
            return ResponseEntity.ok().body(getMessageAdminServices.getMessages(keywords, page, size, ascSort, status, state, type, sentDateStart, sentDateEnd, removedDateStart, removedDateEnd, createdDateStart, createdDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get messages by chat id
        // chatId
        // keywords
        // page
        // size
        // ascSort
        // status
        // state
        // type
        // sentDateStart
        // sentDateEnd
        // removedDateStart
        // removedDateEnd
        // createdDateStart
        // createdDateEnd
    @GetMapping("/chat/{chatId}")
    public ResponseEntity<?> getMessagesByChatId(
        @PathVariable String chatId,
        @RequestParam(required = false, defaultValue = "") String keywords,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "true") boolean ascSort,
        @RequestParam(required = false) Boolean status,
        @RequestParam(required = false) String state,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) Instant sentDateStart,
        @RequestParam(required = false) Instant sentDateEnd,
        @RequestParam(required = false) Instant removedDateStart,
        @RequestParam(required = false) Instant removedDateEnd,
        @RequestParam(required = false) Instant createdDateStart,
        @RequestParam(required = false) Instant createdDateEnd
    ) 
    {
        try {
            return ResponseEntity.ok().body(getMessageAdminServices.getMessagesByChatId(chatId, keywords, page, size, ascSort, status, state, type, sentDateStart, sentDateEnd, removedDateStart, removedDateEnd, createdDateStart, createdDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get messages by chat id
        // chatId
        // keywords
        // page
        // size
        // ascSort
        // status
        // state
        // type
        // sentDateStart
        // sentDateEnd
        // removedDateStart
        // removedDateEnd
        // createdDateStart
        // createdDateEnd
    @GetMapping("/sender/{senderId}")
    public ResponseEntity<?> getMessagesBySenderId(
        @PathVariable String senderId,
        @RequestParam(required = false, defaultValue = "") String keywords,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "true") boolean ascSort,
        @RequestParam(required = false) Boolean status,
        @RequestParam(required = false) String state,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) Instant sentDateStart,
        @RequestParam(required = false) Instant sentDateEnd,
        @RequestParam(required = false) Instant removedDateStart,
        @RequestParam(required = false) Instant removedDateEnd,
        @RequestParam(required = false) Instant createdDateStart,
        @RequestParam(required = false) Instant createdDateEnd
    ) 
    {
        try {
            return ResponseEntity.ok().body(getMessageAdminServices.getMessagesBySenderId(senderId, keywords, page, size, ascSort, status, state, type, sentDateStart, sentDateEnd, removedDateStart, removedDateEnd, createdDateStart, createdDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get message by id
        // messageId
    @GetMapping("/{messageId}")
    public ResponseEntity<?> getMessageById(@PathVariable String messageId) {
        try {
            return ResponseEntity.ok().body(getMessageAdminServices.getMessageById(messageId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get message resource by id
        // messageId
    @GetMapping("/{messageId}/resource")
    public ResponseEntity<?> getMessageFile(@PathVariable String messageId) {
        try {
            ResourceData data = getMessageAdminServices.getMessageFile(messageId);

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
        // message create request
            // chat id
            // content
            // senderId
    @PostMapping("")
    public ResponseEntity<?> postNewTextMessage(@RequestBody TextMessageAdminRequest textMessageRequest) {
        try {
            return ResponseEntity.ok().body(controlMessageAdminServices.addNewTextMessage(textMessageRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // post new file message
        // chat id
        // type
        // file
    @PostMapping(value = "/resource/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> postNewFileMessage(@RequestHeader String senderId, @RequestHeader String chatId, @RequestHeader String type, @RequestPart("file") MultipartFile file) {
        try {
            FileMessageAdminRequest fileMessageRequest = new FileMessageAdminRequest();
            fileMessageRequest.chatId = chatId;
            fileMessageRequest.senderId = senderId;
            fileMessageRequest.type = type;
            fileMessageRequest.file = file;
            return ResponseEntity.ok().body(controlMessageAdminServices.addNewFileMessage(fileMessageRequest));
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
        // message id
        // content
    @PutMapping("/{messageId}/edit")
    public ResponseEntity<?> putTextMessage(@PathVariable String messageId, @RequestBody MessageUpdateContentRequest content) {
        try {
            return ResponseEntity.ok(controlMessageAdminServices.editTextMessage(messageId, content.content));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{messageId}/activate")
    public ResponseEntity<?> activateTextMessage(@PathVariable String messageId) {
        try {
            return ResponseEntity.ok(controlMessageAdminServices.activateMessage(messageId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // soft delete message
        // message id
    @DeleteMapping("/{messageId}/remove")
    public ResponseEntity<?> softDeleteMessage(@PathVariable String messageId) {
        try {
            controlMessageAdminServices.softDeleteMessage(messageId);
            return ResponseEntity.ok().body("Message removed successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // hard delete message
        // message id
    @DeleteMapping("/{messageId}")
    public ResponseEntity<?> hardDeleteMessage(@PathVariable String messageId) {
        try {
            controlMessageAdminServices.hardDeleteMessage(messageId);
            return ResponseEntity.ok().body("Message deleted successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
