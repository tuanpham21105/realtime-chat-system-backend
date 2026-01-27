package com.chat_system.api_gateway.presentation.dto.request.user_profile;

import com.chat_system.api_gateway.presentation.dto.request.account.AccountRequest;

public class UserProfileCreateRequest {
    public AccountRequest account;
    public UserProfileRequest userProfile;
}
