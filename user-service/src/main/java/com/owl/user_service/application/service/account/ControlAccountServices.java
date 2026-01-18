package com.owl.user_service.application.service.account;

import org.springframework.stereotype.Service;

import com.owl.user_service.application.service.user_profile.GetUserProfileServices;
import com.owl.user_service.domain.service.AccountServices;
import com.owl.user_service.persistence.jpa.entity.Account;
import com.owl.user_service.persistence.jpa.repository.AccountJpaRepository;
import com.owl.user_service.presentation.dto.request.AccountRequest;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class ControlAccountServices {
    private final AccountServices accountServices;
    private final AccountJpaRepository accountRepository;

    public ControlAccountServices(AccountJpaRepository _accountRepository, GetUserProfileServices _getUserProfileServices) {
        this.accountRepository = _accountRepository;
        accountServices = new AccountServices();
    }

    public Account addAccount(AccountRequest accountRequest) {
        try {
            Account lastAccount = accountRepository.count() == 0 ? null : accountRepository.findFirstByOrderByIdDesc();
            return accountRepository.save(accountServices.CreateNewAccount(lastAccount != null ? lastAccount.getId() : null, accountRequest));
        }
        catch (IllegalArgumentException ex) {
            throw ex;
        }
    }

    public Account updateAccount(String id, AccountRequest accountRequest) {
        if (!accountServices.ValidateID(id)) {
            throw new IllegalArgumentException("Id is invalid: " + id);
        }

        if (accountRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Account with id " + id + " does not exist");
        }

        if (!accountServices.ValidateUsername(accountRequest.getUsername())) {
            throw new IllegalArgumentException("Username is invalid");
        }

        if (!accountServices.ValidatePassword(accountRequest.getPassword())) {
            throw new IllegalArgumentException("Password is invalid");
        }

        if (!accountServices.ValidateRole(accountRequest.getRole())) {
            throw new IllegalArgumentException("Role is invalid");
        }

        return accountRepository.save(new Account(id, true, Account.AccountRole.valueOf(accountRequest.getRole()), accountRequest.getUsername(), accountRequest.getPassword()));
    }

    public Account updateAccountStatus(String id, boolean status) {
        if (!accountServices.ValidateID(id)) {
            throw new IllegalArgumentException("Id is invalid: " + id);
        }

        if (accountRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Account with id " + id + " does not exist");
        }

        Account account = accountRepository.findById(id).orElse(null);

        accountRepository.updateUpdatedDateById(id);

        return accountRepository.save(new Account(id, status, account.getRole(), account.getUsername(), account.getPassword()));
    }

    public void deleteAccount(String id) {
        if (!accountServices.ValidateID(id)) {
            throw new IllegalArgumentException("Id is invalid: " + id);
        }

        if (accountRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Account with id " + id + " does not exist");
        }
        
        accountRepository.deleteById(id);
    }
}
