package com.chat_system.api_gateway.presentation.rest.chat_member;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chat_system.api_gateway.external_service.client.ChatMemberAdminWebClient;
import com.chat_system.api_gateway.presentation.dto.request.chat_member.ChatMemberAdminRequest;
import com.chat_system.api_gateway.presentation.dto.request.chat_member.ChatMemberUpdateNicknameRequest;
import com.chat_system.api_gateway.presentation.dto.request.chat_member.ChatMemberUpdateRoleRequest;

@RestController
@RequestMapping("/admin/member")
public class ChatMemberAdminController {
    private final ChatMemberAdminWebClient chatMemberAdminWebClient;

    public ChatMemberAdminController(ChatMemberAdminWebClient chatMemberAdminWebClient) {
        this.chatMemberAdminWebClient = chatMemberAdminWebClient;
    }

    // get chat members
        // token 
        // specification
    @GetMapping("")
    public ResponseEntity<?> getChatMember(
        @RequestHeader String token,
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
            return ResponseEntity.ok().body(chatMemberAdminWebClient.getChatMembers(keywords, page, size, ascSort, role, joinDateStart, joinDateEnd));
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
    public ResponseEntity<?> getChatMemberByChatId(
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
            return ResponseEntity.ok().body(chatMemberAdminWebClient.getChatMembersByChatId(chatId, keywords, page, size, ascSort, role, joinDateStart, joinDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get chat members by member id
        // token
        // member id
        // specification
    @GetMapping("/{memberId}")
    public ResponseEntity<?> getChatMemberByMemberId(
        @RequestHeader String token,
        @PathVariable String memberId,
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
            return ResponseEntity.ok().body(chatMemberAdminWebClient.getChatMembersByMemberId(memberId, keywords, page, size, ascSort, role, joinDateStart, joinDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get chat member by chat id and member id
        // token
        // chat id
        // member id
    @GetMapping("/{memberId}/chat/{chatId}")
    public ResponseEntity<?> getChatMemberByChatIdAndMemberId(@RequestHeader String token, @PathVariable String memberId, @PathVariable String chatId) {
        try {
            return ResponseEntity.ok().body(chatMemberAdminWebClient.getChatMemberByChatAndMember(chatId, memberId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // post new chat member 
        // token
        // new chat member
    @PostMapping("")
    public ResponseEntity<?> postChatMember(@RequestHeader String token, @RequestBody ChatMemberAdminRequest chatMemberCreateRequest) {
        try {
            return ResponseEntity.ok().body(chatMemberAdminWebClient.createChatMember(chatMemberCreateRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // update chat member
        // token
        // chat id
        // member id
        // update chat member
    @PutMapping("/{memberId}/chat/{chatId}")
    public ResponseEntity<?> putChatMember(@RequestHeader String token, @PathVariable String memberId, @PathVariable String chatId, @RequestBody ChatMemberAdminRequest chatMemberRequest) {
        try {
            return ResponseEntity.ok().body(chatMemberAdminWebClient.updateChatMember(chatId, memberId, chatMemberRequest));
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
            return ResponseEntity.ok().body(chatMemberAdminWebClient.updateChatMemberRole(chatId, memberId, role.role));
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
    public ResponseEntity<?> patchChatMemberNickname(@RequestHeader String token, @PathVariable String memberId, @PathVariable String chatId, @RequestBody ChatMemberUpdateNicknameRequest nickname) {
        try {
            return ResponseEntity.ok().body(chatMemberAdminWebClient.updateChatMemberNickname(chatId, memberId, nickname.nickname));
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
    public ResponseEntity<?> deleteChatMember(@RequestHeader String token, @PathVariable String memberId, @PathVariable String chatId) {
        try {
            chatMemberAdminWebClient.deleteChatMember(chatId, memberId);
            return ResponseEntity.ok().body("Chat member deleted successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
