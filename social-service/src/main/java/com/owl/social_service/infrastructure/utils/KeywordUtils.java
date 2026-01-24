package com.owl.social_service.infrastructure.utils;

import java.util.Arrays;
import java.util.List;

public class KeywordUtils {

    public static List<String> parseKeywords(String input) {
        if (input == null || input.isBlank()) {
            return List.of();
        }

        return Arrays.stream(input.trim().split("\\s+"))
                .map(String::toLowerCase)
                .distinct()
                .toList();
    }

}
