package com.chat_system.api_gateway.domain.account;

import com.chat_system.api_gateway.external_service.dto.response.user_service.AccountDto.AccountRole;

public class AccountRoleService {
    public static int compareRole(AccountRole a, AccountRole b) {
        int ra = convertRoleToInt(a);
        int rb = convertRoleToInt(b);

        if (ra > rb) {
            return 1;
        }
        else if (ra == rb) {
            return 0;
        }
        else {
            return -1;
        }
    }

    private static int convertRoleToInt(AccountRole r) {
        switch (r) {
            case ADMIN:
                return -1;
            case USER:
                return -2;
            default:
                return -2;
        }
    }
}
