package com.owl.chat_service.presentation.rest.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.owl.chat_service.application.service.user.chat_member.ControlChatMemberUserServices;
import com.owl.chat_service.application.service.user.chat_member.GetChatMemberUserServices;
import com.owl.chat_service.presentation.dto.ChatMemberUpdateNicknameRequest;
import com.owl.chat_service.presentation.dto.ChatMemberUpdateRoleRequest;
import com.owl.chat_service.presentation.dto.user.ChatMemberCreateUserRequest;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/member")
public class ChatMemberUserController {

    private final ControlChatMemberUserServices controlChatMemberUserServices;
    private final GetChatMemberUserServices getChatMemberUserServices;

    public ChatMemberUserController(GetChatMemberUserServices getChatMemberUserServices, ControlChatMemberUserServices controlChatMemberUserServices) {
        this.getChatMemberUserServices = getChatMemberUserServices;
        this.controlChatMemberUserServices = controlChatMemberUserServices;
    }

    // get chat member by member id
        // requester id
        // member id
        // keywords
        // page
        // size
        // ascSort
    @GetMapping("/")
    public ResponseEntity<?> getChatMembersByMemberId(
        @RequestHeader String requesterId,
        @RequestParam(required = false, defaultValue = "") String keywords,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "true") boolean ascSort,
        @RequestParam(required = false) String role,
        @RequestParam(required = false) Instant joinDateStart,
        @RequestParam(required = false) Instant joinDateEnd

    ) {
        try {
            return ResponseEntity.ok().body(getChatMemberUserServices.getChatMembersByMemberId(requesterId, keywords, page, size, ascSort, role, joinDateStart, joinDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get chat member by chat id
        // requester id
        // chat id
        // keywords
        // page
        // size
        // ascSort
        // role
        // joinDateStart
        // joinDateEnd
    @GetMapping("/chat/{chatId}")
    public ResponseEntity<?> getChatMembersByChatId(
        @RequestHeader String requesterId, 
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
            return ResponseEntity.ok().body(getChatMemberUserServices.getChatMembersByChatId(requesterId, chatId, keywords, page, size, ascSort, role, joinDateStart, joinDateEnd));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get chat member by chat id and member id
        // requester id
        // chat id
        // member id
    @GetMapping("/{memberId}/chat/{chatId}")
    public ResponseEntity<?> getChatMemberByChatIdAndMemberId(@RequestHeader String requesterId, @PathVariable String memberId, @PathVariable String chatId) {
        try {
            return ResponseEntity.ok().body(getChatMemberUserServices.getChatMemberByChatIdAndMemberId(requesterId, memberId, chatId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // post chat member
        // requester id
        // chat member create request
            // member id
            // chat id
    @PostMapping("")
    public ResponseEntity<?> postChatMember(@RequestHeader String requesterId, @RequestBody ChatMemberCreateUserRequest chatMemberCreateRequest) {
        try {
            return ResponseEntity.ok().body(controlChatMemberUserServices.addNewChatMember(requesterId, chatMemberCreateRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }   
    }

    // patch role
        // requester id
        // member id
        // chat id
        // role
    @PatchMapping("/{memberId}/chat/{chatId}/role")
    public ResponseEntity<?> patchChatMemberRole(@RequestHeader String requesterId, @PathVariable String memberId, @PathVariable String chatId, @RequestBody ChatMemberUpdateRoleRequest role) {
        try {
            return ResponseEntity.ok().body(controlChatMemberUserServices.updateChatMemberRole(requesterId, memberId, chatId, role.role));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }   
    }

    // patch nickname
        // requester id
        // member id
        // chat id
        // nickname
    @PatchMapping("/{memberId}/chat/{chatId}/nickname")
    public ResponseEntity<?> patchChatMemberNickname(@RequestHeader String requesterId, @PathVariable String memberId, @PathVariable String chatId, @RequestBody ChatMemberUpdateNicknameRequest nickname)  {
        try {
            return ResponseEntity.ok().body(controlChatMemberUserServices.updateChatMemberNickname(requesterId, memberId, chatId, nickname.nickname));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }   
    }

    // delete chat member
        // requester id
        // member id
        // chat id
    @DeleteMapping("/{memberId}/chat/{chatId}")
    public ResponseEntity<?> deleteChatMember(@RequestHeader String requesterId, @PathVariable String memberId, @PathVariable String chatId)  {
        try {
            controlChatMemberUserServices.deleteChatMember(requesterId, memberId, chatId);
            return ResponseEntity.ok().body("Chat member deleted successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }   
    }
}
