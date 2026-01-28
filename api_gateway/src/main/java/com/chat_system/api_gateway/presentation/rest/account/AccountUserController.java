package com.chat_system.api_gateway.presentation.rest.account;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chat_system.api_gateway.external_service.client.AccountWebClient;
import com.chat_system.api_gateway.presentation.dto.request.account.AccountRequest;

@RestController
@RequestMapping("/account")
public class AccountUserController {
    private final AccountWebClient accountWebClient;

    public AccountUserController(AccountWebClient accountWebClient) {
        this.accountWebClient = accountWebClient;
    }

    // get user account
        // token
    @GetMapping("")
    public ResponseEntity<?> getUserAccount(@RequestHeader String token) {
        try {
            return ResponseEntity.ok(accountWebClient.getAccountById(token));
        }
        catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    
    // update user account
        // token
        // update account
    @PutMapping("")
    public ResponseEntity<?> updateUserAccount(@RequestHeader String token, @RequestBody AccountRequest accountRequest) {
        try {
            return ResponseEntity.ok(accountWebClient.updateAccount(token, accountRequest));
        }
        catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // delete user account
        // token
    @DeleteMapping("")
    public ResponseEntity<?> deleteUserAccount(@RequestHeader String token) {
        try {
            return ResponseEntity.ok(accountWebClient.updateAccountStatus(token, false));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
