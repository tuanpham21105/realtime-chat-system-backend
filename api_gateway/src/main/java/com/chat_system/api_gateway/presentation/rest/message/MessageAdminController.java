package com.chat_system.api_gateway.presentation.rest.message;

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

import com.chat_system.api_gateway.external_service.client.MessageAdminWebClient;
import com.chat_system.api_gateway.presentation.dto.request.message.FileMessageAdminRequest;
import com.chat_system.api_gateway.presentation.dto.request.message.MessageUpdateContentRequest;
import com.chat_system.api_gateway.presentation.dto.request.message.TextMessageAdminRequest;

@RestController
@RequestMapping("/admin/message")
public class MessageAdminController {
    private final MessageAdminWebClient messageAdminWebClient;

    public MessageAdminController(MessageAdminWebClient messageAdminWebClient) {
        this.messageAdminWebClient = messageAdminWebClient;
    }

    // get messages
        // token
        // specification
    @GetMapping("")
    public ResponseEntity<?> getMessages(
        @RequestHeader String token,
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
            return ResponseEntity.ok().body(messageAdminWebClient.getMessages(keywords, page, size, ascSort, status, state, type, sentDateStart, sentDateEnd, removedDateStart, removedDateEnd, createdDateStart, createdDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get messages by chat id
        // token
        // chat id
        // specification
    @GetMapping("/chat/{chatId}")
    public ResponseEntity<?> getMessagesByChatId(
        @RequestHeader String token,
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
            return ResponseEntity.ok().body(messageAdminWebClient.getMessagesByChatId(chatId, keywords, page, size, ascSort, status, state, type, sentDateStart, sentDateEnd, removedDateStart, removedDateEnd, createdDateStart, createdDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get messages by sender id
        // token
        // sender id
        // specification
    @GetMapping("/sender/{senderId}")
    public ResponseEntity<?> getMessagesBySenderId(
        @RequestHeader String token,
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
            return ResponseEntity.ok().body(messageAdminWebClient.getMessagesBySenderId(senderId, keywords, page, size, ascSort, status, state, type, sentDateStart, sentDateEnd, removedDateStart, removedDateEnd, createdDateStart, createdDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get message by id
        // token
        // message id
    @GetMapping("/{messageId}")
    public ResponseEntity<?> getMessageById(@RequestHeader String token, @PathVariable String messageId) {
        try {
            return ResponseEntity.ok().body(messageAdminWebClient.getMessageById(messageId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get message file by id
        // token
        // message id
    @GetMapping("/{messageId}/resource")
    public ResponseEntity<?> getMessageFile(@RequestHeader String token, @PathVariable String messageId) {
        try {
            ResponseEntity<Resource> data = messageAdminWebClient.getMessageFile(messageId);

            return ResponseEntity.ok()
                .headers(data.getHeaders())
                .body(data.getBody() /* avatar resource */);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // post new text message   
        // token
        // sender id
        // chat id
        // content
    @PostMapping("")
    public ResponseEntity<?> postNewTextMessage(@RequestHeader String token, @RequestBody TextMessageAdminRequest textMessageRequest) {
        try {
            return ResponseEntity.ok().body(messageAdminWebClient.createTextMessage(textMessageRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // post new file message
        // token
        // sender id
        // chat id
        // file type
        // message file
    @PostMapping(value = "/resource/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> postNewFileMessage(@RequestHeader String token, @RequestHeader String senderId, @RequestHeader String chatId, @RequestHeader String type, @RequestPart("file") MultipartFile file) {
        try {
            FileMessageAdminRequest fileMessageRequest = new FileMessageAdminRequest();
            fileMessageRequest.chatId = chatId;
            fileMessageRequest.senderId = senderId;
            fileMessageRequest.type = type;
            fileMessageRequest.file = file;
            return ResponseEntity.ok().body(messageAdminWebClient.uploadFileMessage(senderId, chatId, type, file));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSize(MaxUploadSizeExceededException e) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File size exceeds 10MB limit");
    }

    // edit text message
        // token
        // message id
        // new message content
    @PutMapping("/{messageId}/edit")
    public ResponseEntity<?> putTextMessage(@RequestHeader String token, @PathVariable String messageId, @RequestBody MessageUpdateContentRequest content) {
        try {
            return ResponseEntity.ok(messageAdminWebClient.editTextMessage(messageId, content));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // activate soft delete message
    @PatchMapping("/{messageId}/activate")
    public ResponseEntity<?> activateTextMessage(@RequestHeader String token, @PathVariable String messageId) {
        try {
            return ResponseEntity.ok(messageAdminWebClient.activateMessage(messageId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // soft delete message
        // token
        // message id
    @DeleteMapping("/{messageId}/remove")
    public ResponseEntity<?> softDeleteMessage(@RequestHeader String token, @PathVariable String messageId) {
        try {
            messageAdminWebClient.softDeleteMessage(messageId);
            return ResponseEntity.ok().body("Message removed successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // hard delete message 
        // token
        // message id
    @DeleteMapping("/{messageId}")
    public ResponseEntity<?> hardDeleteMessage(@RequestHeader String token, @PathVariable String messageId) {
        try {
            messageAdminWebClient.hardDeleteMessage(messageId);
            return ResponseEntity.ok().body("Message deleted successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
