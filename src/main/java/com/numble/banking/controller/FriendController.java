package com.numble.banking.controller;

import com.numble.banking.dto.FriendResponseDto;
import com.numble.banking.service.FriendService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friend")

public class FriendController {

    private final FriendService service;

    @GetMapping("/{loginId}")
    public List<FriendResponseDto> getFriends(@PathVariable String loginId) {
        return service.getFriends(loginId);
    }

}
