package com.owl.user_service.infrastructure.config;

import java.security.SecureRandom;

public class AuthenticationServicesConfig {
    public static final SecureRandom RANDOM = new SecureRandom();
    public static final char[] HEX = "0123456789abcdef".toCharArray();
}
