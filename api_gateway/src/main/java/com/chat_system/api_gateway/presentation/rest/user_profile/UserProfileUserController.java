package com.chat_system.api_gateway.presentation.rest.user_profile;

import java.time.LocalDate;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.chat_system.api_gateway.external_service.client.UserProfileWebClient;
import com.chat_system.api_gateway.presentation.dto.request.user_profile.UserProfileRequest;


@RestController
@RequestMapping("/user")
public class UserProfileUserController {
    private final UserProfileWebClient userProfileWebClient;

    public UserProfileUserController(UserProfileWebClient userProfileWebClient) {
        this.userProfileWebClient = userProfileWebClient;
    }

    // get user profiles
        // token
        // specification
    @GetMapping("")
    public ResponseEntity<?> getProfiles(
        @RequestHeader String token,
        @RequestParam(required = false, defaultValue = "") String keywords, 
        @RequestParam(required = false, defaultValue = "0") int page, 
        @RequestParam(required = false, defaultValue = "10") int size, 
        @RequestParam(required =  false, defaultValue = "0") int gender, 
        @RequestParam(required =  false, defaultValue = "") LocalDate dateOfBirthStart, 
        @RequestParam(required =  false, defaultValue = "") LocalDate dateOfBirthEnd, 
        @RequestParam(required =  false, defaultValue = "true") boolean ascSort
    ) 
    {
        try 
        {
            return ResponseEntity.ok(userProfileWebClient.getProfiles(keywords, page, size, gender, dateOfBirthStart, dateOfBirthEnd, ascSort));    
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get user profile by id
        // token
        // user profile id
    @GetMapping("/{id}")
    public ResponseEntity<?> getProfileById(@RequestHeader String token, @PathVariable String id) {
        try 
        {
            return ResponseEntity.ok(userProfileWebClient.getProfileById(id));    
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get this user profile
        // token
    @GetMapping("/me")
    public ResponseEntity<?> getUserProfile(@RequestHeader String token) {
        try 
        {
            return ResponseEntity.ok(userProfileWebClient.getProfileById(token));    
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get user profile avatar
        // token
        // user profile id    
    @GetMapping("/{id}/avatar")
    public ResponseEntity<?> getUserAvatar(@RequestHeader String token, @PathVariable String id) {
        try {
            ResponseEntity<Resource> data = userProfileWebClient.getAvatar(id);

            return ResponseEntity.ok()
                    .headers(data.getHeaders())
                    .body(data.getBody());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get this user profile avatar
        // token
        // user profile id
    @GetMapping("/me/avatar")
    public ResponseEntity<?> getThisUserAvatar(@RequestHeader String token) {
        try {
            ResponseEntity<Resource> data = userProfileWebClient.getAvatar(token);

            return ResponseEntity.ok()
                    .headers(data.getHeaders())
                    .body(data.getBody());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // update user profile 
        // token
        // update user profile
    @PutMapping("")
    public ResponseEntity<?> updateProfile(@RequestHeader String token, @RequestBody UserProfileRequest userProfileRequest) {
        try {
            return ResponseEntity.ok(userProfileWebClient.updateProfile(token, userProfileRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // update user profile avatar
        // token
        // avatar file
    @PostMapping(value = "/avatar/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadUserAvatar(@RequestHeader String token, @RequestPart("file") MultipartFile avatarFile) {
        try {
            userProfileWebClient.uploadAvatar(token, avatarFile);
            return ResponseEntity.ok("Upload avatar successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSize(MaxUploadSizeExceededException e) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File size exceeds 10MB limit");
    }
}
