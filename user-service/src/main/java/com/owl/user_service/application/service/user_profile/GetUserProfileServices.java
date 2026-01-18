package com.owl.user_service.application.service.user_profile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.owl.user_service.application.etc.UserAvatarData;
import com.owl.user_service.domain.service.AccountServices;
import com.owl.user_service.domain.service.UserProfileServices;
import com.owl.user_service.infrastructure.config.UserProfileServicesConfig;
import com.owl.user_service.infrastructure.utils.KeywordUtils;
import com.owl.user_service.persistence.jpa.entity.UserProfile;
import com.owl.user_service.persistence.jpa.repository.UserProfileJpaRepository;
import com.owl.user_service.persistence.jpa.specification.UserProfileSpecification;

@Service
public class GetUserProfileServices {
    private final UserProfileJpaRepository userProfileRepository;
    private final AccountServices accountServices;
    private final UserProfileServices userProfileServices;

    public GetUserProfileServices(UserProfileJpaRepository _userProfileRepository) {
        this.userProfileRepository = _userProfileRepository;
        accountServices = new AccountServices();
        this.userProfileServices = new UserProfileServices();
    }

    public List<UserProfile> getUserProfiles(String keywords, int page, int size, int gender, LocalDate dateOfBirthStart, LocalDate dateOfBirthEnd, boolean ascSort) {
        if (page < -1 || size <= 0) {
            throw new IllegalArgumentException(
                "Page must be -1 or greater, and size must be greater than 0"
            );
        }
        
        // no pagination
        if (page == -1) {
            List<String> keywordList = KeywordUtils.parseKeywords(keywords);
            return userProfileRepository.findAll(UserProfileSpecification.findUserProfileSpecification(keywordList, gender, dateOfBirthStart, dateOfBirthEnd, ascSort), Sort.by(ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, "id"));
        }

        // pagination
        Pageable pageable = PageRequest.of(page, size, ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, "id");

        if (keywords == null || keywords.isBlank()) {
            return userProfileRepository.findAll(pageable).getContent();
        }

        List<String> keywordList = KeywordUtils.parseKeywords(keywords);
        return userProfileRepository.findAll(UserProfileSpecification.findUserProfileSpecification(keywordList, gender, dateOfBirthStart, dateOfBirthEnd, ascSort), pageable).getContent();
    }

    public UserProfile getUserProfileById(String id) {
        if (accountServices.ValidateID(id) == false) {
            throw new IllegalArgumentException("Id is invalid: " + id);
        }   

        return userProfileRepository.findById(id).orElse(null);
    }

    public String getUserAvatar(String id)  {
        UserProfile profile = userProfileRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));

        String avatar = profile.getAvatar();

        if (avatar == null) {
            throw new IllegalArgumentException("User do not have avatar");
        }
        
        return avatar;
    }

    public UserAvatarData getUserAvatarFile(String id) {
        String avatar = getUserAvatar(id);

        UserAvatarData data = new UserAvatarData();

        Path filePath = UserProfileServicesConfig.AVATAR_STORAGE.resolve(avatar);

        try {
            data.contentType = Files.probeContentType(filePath);
        }
        catch (IOException e) {
            throw new RuntimeException("Cannot get content type", e);
        }
        
        data.resource = userProfileServices.loadAvatar(avatar);

        return data;
    }
}
