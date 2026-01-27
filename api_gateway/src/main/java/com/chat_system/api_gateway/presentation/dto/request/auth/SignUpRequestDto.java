package com.chat_system.api_gateway.presentation.dto.request.auth;

import com.chat_system.api_gateway.presentation.dto.request.account.UserAccountRequest;
import com.chat_system.api_gateway.presentation.dto.request.user_profile.UserProfileRequest;

public class SignUpRequestDto {
    public UserAccountRequest accountRequest;
    public UserProfileRequest userProfileRequest;
}
