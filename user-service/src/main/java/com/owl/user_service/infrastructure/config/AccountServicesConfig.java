package com.owl.user_service.infrastructure.config;

public class AccountServicesConfig {
    public static final String PREFIX = "ACC";
    public static final int NUM_LENGTH = 12;

    public static final String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$";
}
