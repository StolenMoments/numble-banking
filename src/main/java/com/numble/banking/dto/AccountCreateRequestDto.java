package com.numble.banking.dto;

import java.math.BigInteger;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AccountCreateRequestDto {

    private String loginId;
    private BigInteger amount;

    @Builder
    public AccountCreateRequestDto(String loginId, BigInteger amount) {
        this.loginId = loginId;
        this.amount = amount;
    }
}
