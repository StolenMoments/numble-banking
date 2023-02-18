package com.numble.banking.controller;

import com.numble.banking.dto.UserCreateRequestDto;
import com.numble.banking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    @PostMapping
    public Long createUser(@RequestBody UserCreateRequestDto requestDto) {
        return service.createUser(requestDto);
    }

}
