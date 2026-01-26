package com.owl.user_service.domain.service;

import com.owl.user_service.infrastructure.config.AuthenticationServicesConfig;

public class AuthenticationServices {

    public static String generateCode() {
        char[] result = new char[6];

        for (int i = 0; i < 6; i++) {
            result[i] = AuthenticationServicesConfig.HEX[AuthenticationServicesConfig.RANDOM.nextInt(16)];
        }

        return new String(result);
    }
}
