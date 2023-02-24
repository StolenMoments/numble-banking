package com.numble.banking.service;

import com.numble.banking.domain.account.Account;
import com.numble.banking.domain.account.AccountRepository;
import com.numble.banking.domain.user.User;
import com.numble.banking.domain.user.UserRepository;
import com.numble.banking.dto.AccountCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createAccount(AccountCreateRequestDto requestDto) {
        User user = userRepository.findByLoginId(requestDto.getLoginId()).orElseThrow(IllegalArgumentException::new);

        Account account = Account.builder()
            .user(user)
            .balance(requestDto.getAmount())
            .build();

        return accountRepository.save(account).getAccountId();
    }

}
