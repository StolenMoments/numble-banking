package com.numble.banking.dto;

import com.numble.banking.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class FriendResponseDto {

    private Long userId;
    private String loginId;
    private String userName;
    private Long accountId;

    public FriendResponseDto(User user) {
        this.userId = user.getUserId();
        this.loginId = user.getLoginId();
        this.userName = user.getUserName();
        this.accountId = user.getAccount().getAccountId();
    }
}
