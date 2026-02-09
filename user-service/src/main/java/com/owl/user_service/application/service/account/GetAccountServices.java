package com.owl.user_service.application.service.account;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.owl.user_service.domain.service.AccountServices;
import com.owl.user_service.infrastructure.utils.KeywordUtils;
import com.owl.user_service.persistence.jpa.entity.Account;
import com.owl.user_service.persistence.jpa.repository.AccountJpaRepository;
import com.owl.user_service.persistence.jpa.specification.AccountSpecifications;

@Service
public class GetAccountServices {
    private final AccountJpaRepository accountRepository;
    private final AccountServices accountServices;

    public GetAccountServices(AccountJpaRepository _accountRepository) 
    {
        this.accountRepository = _accountRepository;
        accountServices = new AccountServices();
    }

    public List<Account> getAccounts(String keywords, int page, int size, int status, boolean ascSort) {
        if (page < -1 || size <= 0) {
            throw new IllegalArgumentException(
                "Page must be -1 or greater, and size must be greater than 0"
            );
        }

        // no pagination
        if (page == -1) {
            List<String> keywordList = KeywordUtils.parseKeywords(keywords);
            return accountRepository.findAll(AccountSpecifications.findAccountSpecification(keywordList, status), Sort.by(ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, "id"));
        }

        // pagination
        Pageable pageable = PageRequest.of(page, size, ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, "id");

        if (keywords == null || keywords.isBlank()) {
            return accountRepository.findAll(pageable).getContent();
        }

        List<String> keywordList = KeywordUtils.parseKeywords(keywords);
        return accountRepository.findAll(AccountSpecifications.findAccountSpecification(keywordList, status), pageable).getContent();
    }

    public Account getAccountById(String id) {
        if (id == null || id.isEmpty() || !accountServices.ValidateID(id)) {
            throw new IllegalArgumentException("Id is invalid: " + id);
        }
        else {
            return accountRepository.findById(id).orElse(null);
        }
    }
}
