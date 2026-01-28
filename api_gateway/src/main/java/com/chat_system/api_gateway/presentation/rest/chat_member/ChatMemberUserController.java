package com.chat_system.api_gateway.presentation.rest.chat_member;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chat_system.api_gateway.external_service.client.ChatMemberUserWebClient;
import com.chat_system.api_gateway.presentation.dto.request.chat_member.ChatMemberCreateUserRequest;
import com.chat_system.api_gateway.presentation.dto.request.chat_member.ChatMemberUpdateNicknameRequest;
import com.chat_system.api_gateway.presentation.dto.request.chat_member.ChatMemberUpdateRoleRequest;

@RestController
@RequestMapping("/member")
public class ChatMemberUserController {
    private final ChatMemberUserWebClient chatMemberUserWebClient;

    public ChatMemberUserController(ChatMemberUserWebClient chatMemberUserWebClient) {
        this.chatMemberUserWebClient = chatMemberUserWebClient;
    }

    // get user chat members
        // token
        // specification
    @GetMapping("")
    public ResponseEntity<?> getChatMembersByMemberId(
        @RequestHeader String token,
        @RequestParam(required = false, defaultValue = "") String keywords,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "true") boolean ascSort,
        @RequestParam(required = false) String role,
        @RequestParam(required = false) Instant joinDateStart,
        @RequestParam(required = false) Instant joinDateEnd

    ) {
        try {
            return ResponseEntity.ok().body(chatMemberUserWebClient.getChatMembersByMemberId(token, keywords, page, size, ascSort, role, joinDateStart, joinDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get chat members by chat id
        // token
        // chat id
        // specification
    @GetMapping("/chat/{chatId}")
    public ResponseEntity<?> getChatMembersByChatId(
        @RequestHeader String token, 
        @PathVariable String chatId,
        @RequestParam(required = false) String keywords,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "true") boolean ascSort,
        @RequestParam(required = false) String role,
        @RequestParam(required = false) Instant joinDateStart,
        @RequestParam(required = false) Instant joinDateEnd
    ) 
    {
        try {
            return ResponseEntity.ok().body(chatMemberUserWebClient.getChatMembersByChatId(token, chatId, keywords, page, size, ascSort, role, joinDateStart, joinDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get chat member by member id and chat id
        // token
        // member id
        // chat id
    @GetMapping("/{memberId}/chat/{chatId}")
    public ResponseEntity<?> getChatMemberByChatIdAndMemberId(@RequestHeader String token, @PathVariable String memberId, @PathVariable String chatId) {
        try {
            return ResponseEntity.ok().body(chatMemberUserWebClient.getChatMemberByChatIdAndMemberId(token, memberId, chatId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // post new chat member
        // token
        // member id
        // chat id
    @PostMapping("")
    public ResponseEntity<?> postChatMember(@RequestHeader String token, @RequestBody ChatMemberCreateUserRequest chatMemberCreateRequest) {
        try {
            return ResponseEntity.ok().body(chatMemberUserWebClient.createChatMember(token, chatMemberCreateRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }   
    }

    // update chat member role
        // token
        // member id
        // chat id
        // role
    @PatchMapping("/{memberId}/chat/{chatId}/role")
    public ResponseEntity<?> patchChatMemberRole(@RequestHeader String token, @PathVariable String memberId, @PathVariable String chatId, @RequestBody ChatMemberUpdateRoleRequest role) {
        try {
            return ResponseEntity.ok().body(chatMemberUserWebClient.updateRole(token, memberId, chatId, role));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }   
    }

    // update chat member nickname
        // token
        // member id
        // chat id
        // nickname
    @PatchMapping("/{memberId}/chat/{chatId}/nickname")
    public ResponseEntity<?> patchChatMemberNickname(@RequestHeader String token, @PathVariable String memberId, @PathVariable String chatId, @RequestBody ChatMemberUpdateNicknameRequest nickname)  {
        try {
            return ResponseEntity.ok().body(chatMemberUserWebClient.updateNickname(token, memberId, chatId, nickname));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }   
    }

    // delete chat member
        // token
        // member id
        // chat id
    @DeleteMapping("/{memberId}/chat/{chatId}")
    public ResponseEntity<?> deleteChatMember(@RequestHeader String token, @PathVariable String memberId, @PathVariable String chatId)  {
        try {
            chatMemberUserWebClient.deleteChatMember(token, memberId, chatId);
            return ResponseEntity.ok().body("Chat member deleted successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }   
    }
}
