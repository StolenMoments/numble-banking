package com.numble.banking.controller;

import com.numble.banking.dto.AccountCreateRequestDto;
import com.numble.banking.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService service;

    @PostMapping
    public Long createAccount(@RequestBody AccountCreateRequestDto requestDto) {
        return service.createAccount(requestDto);
    }

}
