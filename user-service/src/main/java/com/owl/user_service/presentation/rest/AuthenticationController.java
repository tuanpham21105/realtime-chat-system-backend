package com.owl.user_service.presentation.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.owl.user_service.application.service.account.GetAccountServices;
import com.owl.user_service.application.service.auth.ControlAuthenticationServices;
import com.owl.user_service.application.service.user_profile.ControlUserProfileServices;
import com.owl.user_service.presentation.dto.request.SignInRequestDto;
import com.owl.user_service.presentation.dto.request.SignUpAuthenticateRequest;
import com.owl.user_service.presentation.dto.request.SignUpRequestDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final ControlAuthenticationServices controlAuthenticationServices;
    private final GetAccountServices getAccountServices;
    private final ControlUserProfileServices controlUserProfileServices;

    public AuthenticationController(ControlAuthenticationServices controlAuthenticationServices, GetAccountServices getAccountServices, ControlUserProfileServices controlUserProfileServices) {
        this.controlAuthenticationServices = controlAuthenticationServices;
        this.getAccountServices = getAccountServices;
        this.controlUserProfileServices = controlUserProfileServices;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInRequestDto request) {
        try {
            return ResponseEntity.ok(getAccountServices.signIn(request));
        }
        catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequestDto request) {
        try 
        {
            return ResponseEntity.ok(controlUserProfileServices.signUp(request));  
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/authentic/{accountId}")
    public ResponseEntity<?> authenticateSignUp(@PathVariable String accountId, @RequestBody SignUpAuthenticateRequest request) {
        try 
        {
            controlAuthenticationServices.signUpCodeAuthentic(accountId, request.code);
            return ResponseEntity.ok("Successfully");  
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/authentic/renew/{accountId}")
    public ResponseEntity<String> renewAuthenticSignUpCode (@PathVariable String accountId) {
        try 
        {
            controlAuthenticationServices.renewSignUpAuthenticateCode(accountId);
            return ResponseEntity.ok("Successfully");  
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
