package com.numble.banking.dto;

import com.numble.banking.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
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

    public User toEntity() {
        return User.builder()
            .loginId(this.loginId)
            .password(this.getPassword())
            .userName(this.getUserName())
            .build();
    }
}
