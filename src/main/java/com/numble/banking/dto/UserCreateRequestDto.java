package com.numble.banking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class UserCreateRequestDto {

    private String loginId;
    private String password;
    private String userName;

    @Builder
    public UserCreateRequestDto(String loginId, String password, String userName) {
        this.loginId = loginId;
        this.password = password;
        this.userName = userName;
    }
}
