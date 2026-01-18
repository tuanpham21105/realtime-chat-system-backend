package com.owl.user_service.application.service.user_profile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.owl.user_service.application.service.account.ControlAccountServices;
import com.owl.user_service.application.service.account.GetAccountServices;
import com.owl.user_service.domain.service.UserProfileServices;
import com.owl.user_service.infrastructure.config.UserProfileServicesConfig;
import com.owl.user_service.persistence.jpa.entity.Account;
import com.owl.user_service.persistence.jpa.entity.UserProfile;
import com.owl.user_service.persistence.jpa.repository.UserProfileJpaRepository;
import com.owl.user_service.presentation.dto.request.UserProfileCreateRequest;
import com.owl.user_service.presentation.dto.request.UserProfileRequest;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ControlUserProfileServices {
    private final UserProfileJpaRepository userProfileJpaRepository;
    private final ControlAccountServices controlAccountServices;
    private final UserProfileServices userProfileServices;
    private final GetUserProfileServices getUserProfileServices;
    private final GetAccountServices getAccountServices;

    public ControlUserProfileServices(UserProfileJpaRepository userProfileJpaRepository, ControlAccountServices controlAccountServices, GetUserProfileServices _getUserProfileServices, GetAccountServices _getAccountServices) {
        this.userProfileJpaRepository = userProfileJpaRepository;
        this.controlAccountServices = controlAccountServices;
        this.userProfileServices = new UserProfileServices();
        this.getUserProfileServices = _getUserProfileServices;
        this.getAccountServices = _getAccountServices;
    }

    public UserProfile addUserProfile(UserProfileCreateRequest userProfileCreateRequest) {
        try 
        {
            Account newAccount = controlAccountServices.addAccount(userProfileCreateRequest.getAccount());

            return userProfileJpaRepository.save(userProfileServices.CreateNewUserProfile(newAccount, userProfileCreateRequest.getUserProfile()));
        }
        catch (Exception e) {
            throw e;
        }
    }

    public UserProfile addUserProfileToAccount(String id, UserProfileRequest userProfileRequest) {
        Account account = getAccountServices.getAccountById(id);

        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }

        if (getUserProfileServices.getUserProfileById(id) != null) {
            throw new IllegalArgumentException("User profile already exists");
        }

        try {
            return userProfileJpaRepository.save(userProfileServices.CreateNewUserProfile(account, userProfileRequest));
        }
        catch (Exception e) {
            throw e;
        }
    }

    public UserProfile updateUserProfile(String id, UserProfileRequest userProfileRequest) {
        UserProfile existingUserProfile = userProfileJpaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User profile not found"));

        if (!userProfileServices.ValidateName(userProfileRequest.getName())) {
            throw new IllegalArgumentException("Invalid name");
        }

        if (!userProfileServices.ValidateEmail(userProfileRequest.getEmail())) {
            throw new IllegalArgumentException("Invalid email");
        }

        if (!userProfileServices.ValidatePhoneNumber(userProfileRequest.getPhoneNumber())) {
            throw new IllegalArgumentException("Invalid phone number");
        }

        UserProfile updatedUserProfile = new UserProfile(
            existingUserProfile.getAccount(),
            userProfileRequest.getName(),
            userProfileRequest.getGender(),
            userProfileRequest.getDateOfBirth(),
            existingUserProfile.getAvatar(),
            userProfileRequest.getEmail(),
            userProfileRequest.getPhoneNumber()
        );

        return userProfileJpaRepository.save(updatedUserProfile);
    }

    public void deleteUserProfile(String id) {
        if (!userProfileJpaRepository.existsById(id)) {
            throw new IllegalArgumentException("User profile not found");
        }

        String avatar = getUserProfileServices.getUserProfileById(id).getAvatar();

        if (avatar != null && !avatar.isEmpty()) {
            deleteUserAvatarFile(avatar);
        }

        userProfileJpaRepository.deleteById(id);
    }

    public UserProfile updateAvatarUserProfile(String id, String avatar) {
        UserProfile existingUserProfile = userProfileJpaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User profile not found"));

        UserProfile updatedUserProfile = new UserProfile(
            existingUserProfile.getAccount(),
            existingUserProfile.getName(),
            existingUserProfile.getGender(),
            existingUserProfile.getDateOfBirth(),
            avatar,
            existingUserProfile.getEmail(),
            existingUserProfile.getPhoneNumber()
        );

        existingUserProfile.copyFrom(updatedUserProfile);
        
        return userProfileJpaRepository.save(existingUserProfile);
    }

    public void deleteUserAvatarFile(String avatarFileName) {
        if (avatarFileName == null || avatarFileName.isEmpty()) {
            throw new IllegalArgumentException("User profile don't have avatar");
        }

        Path target = UserProfileServicesConfig.AVATAR_STORAGE.resolve(avatarFileName);

        if (Files.exists(target)) {
            try {
                Files.delete(target);
            }
            catch (IOException e) {
                throw new RuntimeException("Failed to delete avatar file", e);
            }
        }
        else {
            throw new IllegalArgumentException("User profile avatar not found");
        }
    }

    public void updateUserAvatarFile(String id, MultipartFile file) {
        if (getUserProfileServices.getUserProfileById(id) == null) {
            throw new IllegalArgumentException("User profile does not exists");
        }

        userProfileServices.ValidateAvatarFileMetaData(file);

        userProfileServices.ValidateAvatarFileType(file);

        String avatar = getUserProfileServices.getUserProfileById(id).getAvatar();

        if (avatar != null && !avatar.isEmpty()) {
            deleteUserAvatarFile(avatar);
        }

        updateAvatarUserProfile(id, userProfileServices.SaveUserAvatarFile(id, file));
    }
}
