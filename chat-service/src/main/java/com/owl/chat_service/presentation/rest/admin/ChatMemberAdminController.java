package com.owl.chat_service.presentation.rest.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.owl.chat_service.application.service.admin.chat_member.ControlChatMemberAdminSerivces;
import com.owl.chat_service.application.service.admin.chat_member.GetChatMemberAdminServices;
import com.owl.chat_service.presentation.dto.ChatMemberUpdateNicknameRequest;
import com.owl.chat_service.presentation.dto.ChatMemberUpdateRoleRequest;
import com.owl.chat_service.presentation.dto.admin.ChatMemberAdminRequest;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/admin/member")
public class ChatMemberAdminController {

    private final ControlChatMemberAdminSerivces controlChatMemberAdminSerivces;
    private final GetChatMemberAdminServices getChatMemberAdminServices;

    public ChatMemberAdminController(GetChatMemberAdminServices getChatMemberAdminServices, ControlChatMemberAdminSerivces controlChatMemberAdminSerivces) {
        this.getChatMemberAdminServices = getChatMemberAdminServices;
        this.controlChatMemberAdminSerivces = controlChatMemberAdminSerivces;
    }

    // get chat member
        // keywords
        // page
        // size
        // ascSort
        // role
        // joinDateStart
        // joinDateEnd
    @GetMapping("")
    public ResponseEntity<?> getChatMember(
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
            return ResponseEntity.ok().body(getChatMemberAdminServices.getChatMembers(keywords, page, size, ascSort, role, joinDateStart, joinDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get chat member by chat id
        // chat id
        // keywords
        // page
        // size
        // ascSort
        // role
        // joinDateStart
        // joinDateEnd
    @GetMapping("/chat/{chatId}")
    public ResponseEntity<?> getChatMemberByChatId(
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
            return ResponseEntity.ok().body(getChatMemberAdminServices.getChatMembersByChatId(chatId, keywords, page, size, ascSort, role, joinDateStart, joinDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get chat member by member id
        // member id
        // keywords
        // page
        // size
        // ascSort
        // role
        // joinDateStart
        // joinDateEnd
    @GetMapping("/{memberId}")
    public ResponseEntity<?> getChatMemberByMemberId(
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
            return ResponseEntity.ok().body(getChatMemberAdminServices.getChatMembersByMemberId(memberId, keywords, page, size, ascSort, role, joinDateStart, joinDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // get chat member by chat id and member id
        // chat id
        // member id
    @GetMapping("/{memberId}/chat/{chatId}")
    public ResponseEntity<?> getMethodName(@PathVariable String memberId, @PathVariable String chatId) {
        try {
            return ResponseEntity.ok().body(getChatMemberAdminServices.getChatMemberByChatIdAndMemberId(chatId, memberId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // post chat member
        // chat member create request
            // chat id
            // member id
            // role
            // nickname
            // inviter id
            // join date
    @PostMapping("")
    public ResponseEntity<?> postChatMember(@RequestBody ChatMemberAdminRequest chatMemberCreateRequest) {
        try {
            return ResponseEntity.ok().body(controlChatMemberAdminSerivces.addNewChatMember(chatMemberCreateRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    // put chat member
        // member id
        // chat id
        // chat member update request
            // chat id
            // member id
            // role
            // nickname
            // inviter id
            // join date
    @PutMapping("/{memberId}/chat/{chatId}")
    public ResponseEntity<?> putChatMember(@PathVariable String memberId, @PathVariable String chatId, @RequestBody ChatMemberAdminRequest chatMemberRequest) {
        try {
            return ResponseEntity.ok().body(controlChatMemberAdminSerivces.updateChatMember(chatId, memberId, chatMemberRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // patch role
        // member id
        // chat id
        // role
    @PatchMapping("/{memberId}/chat/{chatId}/role")
    public ResponseEntity<?> patchChatMemberRole(@PathVariable String memberId, @PathVariable String chatId, @RequestBody ChatMemberUpdateRoleRequest role) {
        try {
            return ResponseEntity.ok().body(controlChatMemberAdminSerivces.updateChatMemberRole(chatId, memberId, role.role));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // patch nickname
        // member id
        // chat id
        // nickname
    @PatchMapping("/{memberId}/chat/{chatId}/nickname")
    public ResponseEntity<?> patchChatMemberNickname(@PathVariable String memberId, @PathVariable String chatId, @RequestBody ChatMemberUpdateNicknameRequest nickname) {
        try {
            return ResponseEntity.ok().body(controlChatMemberAdminSerivces.updateChatMemberNickname(chatId, memberId, nickname.nickname));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // delete chat member
        // member id
        // chat id 
    @DeleteMapping("/{memberId}/chat/{chatId}")
    public ResponseEntity<?> deleteChatMember(@PathVariable String memberId, @PathVariable String chatId) {
        try {
            controlChatMemberAdminSerivces.deleteChatMember(chatId, memberId);
            return ResponseEntity.ok().body("Chat member deleted successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
