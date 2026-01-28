package com.chat_system.api_gateway.presentation.rest.account;

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

import com.chat_system.api_gateway.application.services.account.AccountAdminServices;
import com.chat_system.api_gateway.presentation.dto.request.account.AccountRequest;

@RestController
@RequestMapping("/admin/account")
public class AccountAdminController {
    private final AccountAdminServices accountAdminServices;

    public AccountAdminController(AccountAdminServices accountAdminServices) {
        this.accountAdminServices = accountAdminServices;
    }

    // get accounts
        // token
        // specification
    @GetMapping("")
    public ResponseEntity<?> getAccounts(
        @RequestHeader String token,
        @RequestParam(required = false) String keywords, 
        @RequestParam(required = false, defaultValue = "0") int page, 
        @RequestParam(required = false, defaultValue = "10") int size, 
        @RequestParam(required =  false, defaultValue = "0") int status,
        @RequestParam(required = false, defaultValue = "true") boolean ascSort
    ) 
    {
        try {
            return ResponseEntity.ok(accountAdminServices.getAccounts(keywords, page, size, status, ascSort));
        }
        catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // get account by id
        // token
        // account id
    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@RequestHeader String token, @PathVariable String id) {
        try {
            return ResponseEntity.ok(accountAdminServices.getAccountById(id));
        }
        catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // add account
        // token
        // new account
    @PostMapping("")
    public ResponseEntity<?> addAccount(@RequestHeader String token, @RequestBody AccountRequest newAccountRequest) {
        try {
            return ResponseEntity.ok(accountAdminServices.addAccount(newAccountRequest));
        }
        catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // update account
        // token
        // update account
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccount(@RequestHeader String token, @PathVariable String id, @RequestBody AccountRequest accountRequest) {
        try {
            return ResponseEntity.ok(accountAdminServices.updateAccount(id, accountRequest));
        }
        catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // update account status
        // token
        // status
    @PatchMapping("/{id}/status/{status}")
    public ResponseEntity<?> updateAccountStatus(@RequestHeader String token, @PathVariable String id, @PathVariable boolean status) {
        try {
            return ResponseEntity.ok(accountAdminServices.updateAccountStatus(id, status));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    
    // delete account
        // token
        // account id
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteAccount(@RequestHeader String token, @PathVariable String id) {
        try {
            accountAdminServices.deleteAccount(id);
            return ResponseEntity.ok("Account and User Profile with id " + id + " has been deleted successfully");
        }
        catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
