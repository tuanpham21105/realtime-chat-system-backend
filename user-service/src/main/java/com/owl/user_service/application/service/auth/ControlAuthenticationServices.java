package com.owl.user_service.application.service.auth;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.owl.user_service.application.jwt.JwtTokenService;
import com.owl.user_service.application.service.account.ControlAccountServices;
import com.owl.user_service.application.service.mail.EmailService;
import com.owl.user_service.application.service.user_profile.GetUserProfileServices;
import com.owl.user_service.domain.service.AuthenticationServices;
import com.owl.user_service.persistence.jpa.entity.Account;
import com.owl.user_service.persistence.jpa.entity.SignUpAuthenticateCode;
import com.owl.user_service.persistence.jpa.entity.UserProfile;
import com.owl.user_service.persistence.jpa.repository.AccountJpaRepository;
import com.owl.user_service.persistence.jpa.repository.AuthenticateRepository;
import com.owl.user_service.presentation.dto.request.SignInRequestDto;
import com.owl.user_service.presentation.dto.response.AccessTokenResponseDto;
import com.owl.user_service.presentation.dto.response.SignInResponseDto;

import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ControlAuthenticationServices {
    private final AuthenticateRepository authenticateRepository;
    private final ControlAccountServices controlAccountServices;
    private final EmailService emailService;          
    private final GetUserProfileServices getUserProfileServices;
    private final AccountJpaRepository accountRepository;
    private final JwtTokenService jwtTokenService;

    @Value("${security.jwt.access-expiration}")
    private String accessExpiration;

    public ControlAuthenticationServices(AuthenticateRepository authenticateRepository, ControlAccountServices controlAccountServices, EmailService emailService, GetUserProfileServices getUserProfileServices, AccountJpaRepository accountRepository, JwtTokenService jwtTokenService) {
        this.authenticateRepository = authenticateRepository;
        this.controlAccountServices = controlAccountServices;
        this.emailService = emailService;
        this.getUserProfileServices = getUserProfileServices;
        this.accountRepository = accountRepository;
        this.jwtTokenService = jwtTokenService;
    }

    public SignInResponseDto signIn(SignInRequestDto request) {
        Account account = accountRepository.findByUsername(request.username).orElse(null);

        if (account == null) 
            throw new IllegalArgumentException("Account does not exists");

        if (!account.getStatus())
            throw new IllegalArgumentException("Account is inactive");

        if (account.getPassword().compareTo(request.password) != 0) 
            throw new SecurityException("Wrong password");

        SignInResponseDto response = new SignInResponseDto();
        response.accessToken = jwtTokenService.generateAccessToken(account.getId(), account.getRole().toString());
        response.refreshToken = jwtTokenService.generateRefreshToken(account.getId());
        response.expiresIn = Integer.parseInt(accessExpiration) / 1000;
        response.tokenType = "Bearer";

        return response;
    }

    public void signUpCodeAuthentic(String id, String code) {    
        SignUpAuthenticateCode signUpAuthenticateCode = authenticateRepository.findById(id).orElse(null);

        if (signUpAuthenticateCode == null) 
            throw new IllegalArgumentException("Code not found");

        if (signUpAuthenticateCode.getCreatedDate() == null)
            throw new IllegalArgumentException("Missing created date");

        if (signUpAuthenticateCode.getCreatedDate().plusSeconds(120).isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Code expired");

        if (code.compareToIgnoreCase(signUpAuthenticateCode.getCode()) != 0)
            throw new SecurityException("Code not match");

        controlAccountServices.updateAccountStatus(id, true);

        deleteSignUpAuthenticateCode(id);
    }

    public void newSignUpAuthenticateCode(String id) {
        SignUpAuthenticateCode signUpAuthenticateCode = new SignUpAuthenticateCode();

        UserProfile userProfile = getUserProfileServices.getUserProfileById(id);

        if (userProfile == null) 
            throw new IllegalArgumentException("Account not found");

        signUpAuthenticateCode.setId(id);
        signUpAuthenticateCode.setCode(AuthenticationServices.generateCode());
        signUpAuthenticateCode.setCreatedDate(LocalDateTime.now());

        authenticateRepository.save(signUpAuthenticateCode);

        emailService.sendEmail(userProfile.getEmail(), "Verify your account", "Your verification code is: " + signUpAuthenticateCode.getCode());
    }

    public void renewSignUpAuthenticateCode(String id) {
        SignUpAuthenticateCode existingCode = authenticateRepository.findById(id).orElse(null);

        if (existingCode == null)
            throw new IllegalArgumentException("Old code not found");

        deleteSignUpAuthenticateCode(id);

        newSignUpAuthenticateCode(id);
    }

    public void deleteSignUpAuthenticateCode(String id) {
        authenticateRepository.deleteById(id);
    }

    public AccessTokenResponseDto refreshAccessToken(String refreshToken) {
        Claims claims = jwtTokenService.validateRefreshToken(refreshToken);

        Account account = accountRepository.findById(claims.getSubject()).orElse(null);

        if (account == null)
            throw new IllegalArgumentException("Account not found");
        
        AccessTokenResponseDto accessTokenResponseDto = new AccessTokenResponseDto();

        accessTokenResponseDto.accessToken = jwtTokenService.generateAccessToken(account.getId(), account.getRole().toString());
        
        return accessTokenResponseDto;
    }
}
