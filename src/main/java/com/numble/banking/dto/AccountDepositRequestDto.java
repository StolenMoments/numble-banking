package com.numble.banking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountDepositRequestDto {

    private String loginId;
    private Long accountId;
    private Long amount;

    @Builder
    public AccountDepositRequestDto(String loginId, Long accountId, Long amount) {
        this.loginId = loginId;
        this.accountId = accountId;
        this.amount = amount;
    }
}
