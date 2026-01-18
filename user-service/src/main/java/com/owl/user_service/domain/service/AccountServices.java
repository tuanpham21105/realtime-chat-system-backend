package com.owl.user_service.domain.service;

import com.owl.user_service.infrastructure.config.AccountServicesConfig;
import com.owl.user_service.persistence.jpa.entity.Account;
import com.owl.user_service.presentation.dto.request.AccountRequest;

public class AccountServices {
    private String GenerateNewID(String lastID) {
        // If no last ID, start from 1
        if (lastID == null || lastID.isBlank()) {
            return AccountServicesConfig.PREFIX + String.format("%0" + AccountServicesConfig.NUM_LENGTH + "d", 1);
        }

        // Validate format
        if (!ValidateID(lastID)) {
            throw new IllegalArgumentException("Invalid lastID format: " + lastID);
        }

        // Extract numeric part
        String numericPart = lastID.substring(AccountServicesConfig.PREFIX.length());
        long number = Long.parseLong(numericPart);

        // Increment
        number++;

        // Ensure number does not exceed max length
        if (String.valueOf(number).length() > AccountServicesConfig.NUM_LENGTH) {
            throw new IllegalStateException("ID overflow: cannot generate next ID");
        }

        // Build next ID with leading zeros
        return AccountServicesConfig.PREFIX + String.format("%0" + AccountServicesConfig.NUM_LENGTH + "d", number);
    }

    public boolean ValidateID(String id) {
        if (id == null || id.isBlank()) {
            return false;
        }

        if (!id.startsWith(AccountServicesConfig.PREFIX)) {
            return false;
        }

        String numericPart = id.substring(AccountServicesConfig.PREFIX.length());
        if (numericPart.length() != AccountServicesConfig.NUM_LENGTH) {
            return false;
        }

        if (!id.matches(AccountServicesConfig.PREFIX + "\\d{" + AccountServicesConfig.NUM_LENGTH + "}")) {
            return false;
        }

        try {
            Long.parseLong(numericPart);
        } catch (NumberFormatException ex) {
            return false;
        }

        return true;
    }

    public boolean ValidatePassword(String password) {
        if (password == null || password.isBlank()) {
            return false;
        }

        if (!password.matches(AccountServicesConfig.PASSWORD_REGEX)) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean ValidateUsername(String username) {
        if (username == null || username.isBlank()) {
            return false;
        }

        return true;
    }
    
    public boolean ValidateRole(String role) {
        if (role == null || role.isBlank()) {
            return false;
        }

        if (!role.equals("USER") && !role.equals("ADMIN")) {
            return false;
        }

        return true;
    }

    public Account CreateNewAccount(String lastID, AccountRequest accountRequest) {
        if (!ValidateUsername(accountRequest.getUsername())) {
            throw new IllegalArgumentException("Username is invalid");
        }

        if (!ValidatePassword(accountRequest.getPassword())) {
            throw new IllegalArgumentException("Password is invalid");
        }

        if (!ValidateRole(accountRequest.getRole())) {
            throw new IllegalArgumentException("Role is invalid");
        }

        Account newAccount = new Account(GenerateNewID(lastID), true, Account.AccountRole.valueOf(accountRequest.getRole()), accountRequest.getUsername(), accountRequest.getPassword());

        return newAccount;
    }
}
