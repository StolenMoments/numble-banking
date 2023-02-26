package com.numble.banking.controller;

import com.numble.banking.dto.AccountCreateRequestDto;
import com.numble.banking.dto.AccountDepositRequestDto;
import com.numble.banking.dto.AccountTransferRequestDto;
import com.numble.banking.dto.AccountWithdrawRequestDto;
import com.numble.banking.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("/deposit")
    public Long depositMoney(@RequestBody AccountDepositRequestDto requestDto) {
        return service.depositMoney(requestDto);
    }

    @PostMapping("/withdraw")
    public Long withdrawMoney(@RequestBody AccountWithdrawRequestDto requestDto) {
        return service.withdrawMoney(requestDto);
    }

    @PostMapping("/transfer")
    public Long transferMoney(@RequestBody AccountTransferRequestDto requestDto) {
        return service.transferMoney(requestDto);
    }

    @GetMapping
    public Long getBalance(@RequestParam Long accountId) {
        return service.getBalance(accountId);
    }

}
