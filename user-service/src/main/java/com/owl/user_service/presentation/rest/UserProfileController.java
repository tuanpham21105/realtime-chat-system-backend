package com.owl.user_service.presentation.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.owl.user_service.application.etc.UserAvatarData;
import com.owl.user_service.application.service.user_profile.ControlUserProfileServices;
import com.owl.user_service.application.service.user_profile.GetUserProfileServices;
import com.owl.user_service.presentation.dto.request.UserProfileCreateRequest;
import com.owl.user_service.presentation.dto.request.UserProfileRequest;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/user")
public class UserProfileController {
    private final GetUserProfileServices getUserProfileService;
    private final ControlUserProfileServices controlUserProfileServices;

    public UserProfileController(GetUserProfileServices getUserProfileService, ControlUserProfileServices controlUserProfileServices) {
        this.getUserProfileService = getUserProfileService;
        this.controlUserProfileServices = controlUserProfileServices;
    }

    @GetMapping("")
    public ResponseEntity<?> getProfiles(
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
            return ResponseEntity.ok(getUserProfileService.getUserProfiles(keywords, page, size, gender, dateOfBirthStart, dateOfBirthEnd, ascSort));    
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getProfileById(@PathVariable String id) {
        try 
        {
            return ResponseEntity.ok(getUserProfileService.getUserProfileById(id));    
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("")
    public ResponseEntity<?> addNewProfile(@RequestBody UserProfileCreateRequest userProfileCreateRequest) {
        try 
        {
            return ResponseEntity.ok(controlUserProfileServices.addUserProfile(userProfileCreateRequest));    
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/account/{id}")
    public ResponseEntity<?> addNewProfileToAccount(@PathVariable String id, @RequestBody UserProfileRequest userProfileRequest) {
        try 
        {
            return ResponseEntity.ok(controlUserProfileServices.addUserProfileToAccount(id, userProfileRequest));    
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable String id, @RequestBody UserProfileRequest userProfileRequest) {
        try {
            return ResponseEntity.ok(controlUserProfileServices.updateUserProfile(id, userProfileRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // @PatchMapping("/{id}/avatar")
    // public ResponseEntity<?> updateAvatar(@PathVariable String id, @RequestBody String avatar) {
    //     try {
    //         return ResponseEntity.ok(controlUserProfileServices.updateAvataUserProfile(id, avatar));
    //     }
    //     catch (Exception e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProfile(@PathVariable String id) {
        try 
        {
            controlUserProfileServices.deleteUserProfile(id);
            return ResponseEntity.ok("User profile deleted successfully");  
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/{id}/avatar/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadUserAvatar(@PathVariable String id, @RequestPart("file") MultipartFile avatarFile) {
        try {
            controlUserProfileServices.updateUserAvatarFile(id, avatarFile);
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
    
    @GetMapping("/{id}/avatar")
    public ResponseEntity<?> getUserAvatar(@PathVariable String id) {
        try {
            
            UserAvatarData data = getUserProfileService.getUserAvatarFile(id);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(data.contentType))
                    .body(data.resource);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
