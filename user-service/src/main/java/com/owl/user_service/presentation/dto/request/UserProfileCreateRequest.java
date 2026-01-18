package com.owl.user_service.presentation.dto.request;

public class UserProfileCreateRequest {
    private AccountRequest account;
    private UserProfileRequest userProfile;
    public AccountRequest getAccount() {
        return account;
    }
    public void setAccount(AccountRequest account) {
        this.account = account;
    }
    public UserProfileRequest getUserProfile() {
        return userProfile;
    }
    public void setUserProfile(UserProfileRequest userProfile) {
        this.userProfile = userProfile;
    }
}
