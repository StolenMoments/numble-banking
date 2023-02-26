package com.numble.banking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AccountCreateRequestDto {

    private String loginId;
    private Long amount;

    @Builder
    public AccountCreateRequestDto(String loginId, Long amount) {
        this.loginId = loginId;
        this.amount = amount;
    }
}
