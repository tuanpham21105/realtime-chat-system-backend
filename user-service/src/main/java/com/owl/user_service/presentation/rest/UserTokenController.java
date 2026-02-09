package com.owl.user_service.presentation.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.owl.user_service.application.jwt.JwtTokenService;

import io.jsonwebtoken.Claims;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/token")
public class UserTokenController {
    private final JwtTokenService jwtTokenService;

    public UserTokenController(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("/access")
    public ResponseEntity<Claims> validateAccessToken(@CookieValue String accessToken) {
        return ResponseEntity.ok(jwtTokenService.validateAccessToken(accessToken));
    }
}
