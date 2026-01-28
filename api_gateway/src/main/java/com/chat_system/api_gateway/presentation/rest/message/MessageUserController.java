package com.chat_system.api_gateway.presentation.rest.message;

import java.time.Instant;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
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

import com.chat_system.api_gateway.external_service.client.MessageUserWebClient;
import com.chat_system.api_gateway.presentation.dto.request.message.MessageUpdateContentRequest;
import com.chat_system.api_gateway.presentation.dto.request.message.TextMessageUserRequest;

@RestController
@RequestMapping("/message")
public class MessageUserController {
    private final MessageUserWebClient messageUserWebClient;

    public MessageUserController(MessageUserWebClient messageUserWebClient) {
        this.messageUserWebClient = messageUserWebClient;
    }

    // get messages by chat id
        // token
        // chat id
        // specification
    @GetMapping("/chat/{chatId}")
    public ResponseEntity<?>  getMessagesByChatId(
        @RequestHeader String token,
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
            return ResponseEntity.ok().body(messageUserWebClient.getMessagesByChatId(token, chatId, keywords, page, size, ascSort, type, senderId, sentDateStart, sentDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get messages by message id
        // token 
        // message id
    @GetMapping("/{messageId}")
    public ResponseEntity<?> getMessageById(@RequestHeader String token, @PathVariable String messageId) {
        try {
            return ResponseEntity.ok().body(messageUserWebClient.getMessageById(token, messageId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get nessage file by id
        // token
        // message id
    @GetMapping("/{messageId}/resource")
    public ResponseEntity<?> getMessageFile(@RequestHeader String token, @PathVariable String messageId) {
        try {
            ResponseEntity<Resource> data = messageUserWebClient.getMessageFile(token, messageId);

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
        // chat id
        // content
    @PostMapping("")
    public ResponseEntity<?> postNewTextMessage(@RequestHeader String token, @RequestBody TextMessageUserRequest textMessageRequest) {
        try {
            return ResponseEntity.ok().body(messageUserWebClient.createTextMessage(token, textMessageRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // post new file message
        // token
        // chat id
        // type
        // file
    @PostMapping(value = "/resource/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> postNewFileMessage(@RequestHeader String token,  @RequestHeader String chatId, @RequestHeader String type, @RequestPart("file") MultipartFile file) {
        try {
            return ResponseEntity.ok().body(messageUserWebClient.uploadFileMessage(token, chatId, type, file));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSize(MaxUploadSizeExceededException e) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File size exceeds 10MB limit");
    }

    // edit text message content
        // token
        // message id
        // content
    @PutMapping("/{messageId}/edit")
    public ResponseEntity<?> putTextMessage(@RequestHeader String token, @PathVariable String messageId, @RequestBody MessageUpdateContentRequest content) {
        try {
            return ResponseEntity.ok(messageUserWebClient.editMessage(token, messageId, content));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // soft delete message
        // token
        // message id
    @DeleteMapping("/{messageId}")
    public ResponseEntity<?> softDeleteMessage(@RequestHeader String token, @PathVariable String messageId) {
        try {
            messageUserWebClient.softDeleteMessage(token, messageId);
            return ResponseEntity.ok().body("Message removed successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
