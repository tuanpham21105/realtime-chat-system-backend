package com.chat_system.api_gateway.presentation.rest.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chat_system.api_gateway.application.services.auth.AuthenticationServices;
import com.chat_system.api_gateway.presentation.dto.request.auth.SignInRequestDto;
import com.chat_system.api_gateway.presentation.dto.request.auth.SignUpAuthenticateRequest;
import com.chat_system.api_gateway.presentation.dto.request.auth.SignUpRequestDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationServices authenticationServices;

    public AuthenticationController(AuthenticationServices authenticationServices) {
        this.authenticationServices = authenticationServices;
    }

    // sign in
        // username
        // password
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(
        @RequestBody SignInRequestDto request
    ) {
        try {
            return ResponseEntity.ok(authenticationServices.signIn(request));
        }
        catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // sign up
        // account 
        // user profile
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequestDto request) {
        try 
        {
            return ResponseEntity.ok(authenticationServices.signUp(request));  
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // authentic code
        // id 
        // code
    @PostMapping("/authentic/{accountId}")
    public ResponseEntity<?> authenticateSignUp(@PathVariable String accountId, @RequestBody SignUpAuthenticateRequest request) {
        try 
        {
            authenticationServices.signUpCodeAuthentic(accountId, request.code);
            return ResponseEntity.ok("Successfully");  
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // renew code
        // id
    @GetMapping("/authentic/renew/{accountId}")
    public ResponseEntity<String> renewAuthenticSignUpCode (@PathVariable String accountId) {
        try 
        {
            authenticationServices.renewSignUpAuthenticateCode(accountId);
            return ResponseEntity.ok("Successfully");  
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
