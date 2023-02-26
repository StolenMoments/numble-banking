package com.numble.banking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class AccountTransferRequestDto {

    private String loginId;
    private String receiverLoginId;
    private Long senderAccountId;
    private Long receiverAccountId;
    private Long amount;

    @Builder
    public AccountTransferRequestDto(String loginId, String receiverLoginId, Long senderAccountId,
        Long receiverAccountId, Long amount) {
        this.loginId = loginId;
        this.receiverLoginId = receiverLoginId;
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.amount = amount;
    }
}
