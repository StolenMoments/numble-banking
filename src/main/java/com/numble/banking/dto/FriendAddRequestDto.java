package com.numble.banking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class FriendAddRequestDto {

    private String fromLoginId;
    private String toLoginId;

    @Builder
    public FriendAddRequestDto(String fromLoginId, String toLoginId) {
        this.fromLoginId = fromLoginId;
        this.toLoginId = toLoginId;
    }
}
