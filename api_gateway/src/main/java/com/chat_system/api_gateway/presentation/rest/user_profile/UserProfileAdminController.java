package com.chat_system.api_gateway.presentation.rest.user_profile;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.chat_system.api_gateway.presentation.dto.request.user_profile.UserProfileCreateRequest;
import com.chat_system.api_gateway.presentation.dto.request.user_profile.UserProfileRequest;

@RestController
@RequestMapping("/admin/user")
public class UserProfileAdminController {
    private final UserProfileWebClient userProfileWebClient;

    public UserProfileAdminController(UserProfileWebClient userProfileWebClient) {
        this.userProfileWebClient = userProfileWebClient;
    }

    // get profiles
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

    // get profile by id
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

    // post new user profile
        // token
        // new account
        // new user profile
    @PostMapping("")
    public ResponseEntity<?> addNewProfile(@RequestHeader String token, @RequestBody UserProfileCreateRequest userProfileCreateRequest) {
        try 
        {
            return ResponseEntity.ok(userProfileWebClient.createProfile(userProfileCreateRequest));    
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // post new user profile to account
        // token
        // account id
        // new user profile
    @PostMapping("/account/{id}")
    public ResponseEntity<?> addNewProfileToAccount(@RequestHeader String token, @PathVariable String id, @RequestBody UserProfileRequest userProfileRequest) {
        try 
        {
            return ResponseEntity.ok(userProfileWebClient.addProfileToAccount(id, userProfileRequest));    
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // update user profile
        // token
        // user profile id
        // update user profile
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(@RequestHeader String token, @PathVariable String id, @RequestBody UserProfileRequest userProfileRequest) {
        try {
            return ResponseEntity.ok(userProfileWebClient.updateProfile(id, userProfileRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // delete user profile 
        // token
        // user profile id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProfile(@RequestHeader String token, @PathVariable String id) {
        try 
        {
            userProfileWebClient.deleteProfile(id);
            return ResponseEntity.ok("User profile deleted successfully");  
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // update user profile avatar
        // token
        // user profile id
        // avatar file
    @PostMapping(value = "/{id}/avatar/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadUserAvatar(@RequestHeader String token, @PathVariable String id, @RequestPart("file") MultipartFile avatarFile) {
        try {
            userProfileWebClient.uploadAvatar(id, avatarFile);
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
