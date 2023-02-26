package com.numble.banking.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountWithdrawRequestDto {

    private Long accountId;
    private String loginId;
    private Long amount;

    @Builder
    public AccountWithdrawRequestDto(Long accountId, String loginId, Long amount) {
        this.accountId = accountId;
        this.loginId = loginId;
        this.amount = amount;
    }
}
