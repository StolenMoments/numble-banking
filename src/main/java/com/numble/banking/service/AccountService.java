package com.numble.banking.service;

import com.numble.banking.domain.account.Account;
import com.numble.banking.domain.account.AccountRepository;
import com.numble.banking.domain.user.User;
import com.numble.banking.domain.user.UserRepository;
import com.numble.banking.dto.AccountCreateRequestDto;
import com.numble.banking.dto.AccountDepositRequestDto;
import com.numble.banking.dto.AccountTransferRequestDto;
import com.numble.banking.dto.AccountWithdrawRequestDto;
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
        User user = userRepository.findByLoginId(requestDto.getLoginId())
            .orElseThrow(IllegalArgumentException::new);

        Account account = Account.builder()
            .user(user)
            .balance(requestDto.getAmount())
            .build();

        return accountRepository.save(account).getAccountId();
    }

    @Transactional
    public Long transferMoney(AccountTransferRequestDto requestDto) {
        User sender = userRepository.findByLoginId(requestDto.getLoginId())
            .orElseThrow(IllegalArgumentException::new);

        User receiver = userRepository.findByLoginId(requestDto.getReceiverLoginId())
            .orElseThrow(IllegalArgumentException::new);

        Account senderAccount = accountRepository.findByUser(sender)
            .orElseThrow(IllegalArgumentException::new);
        Account receiverAccount = accountRepository.findByUser(receiver)
            .orElseThrow(IllegalArgumentException::new);

        withdrawMoney(
            AccountWithdrawRequestDto.builder().accountId(senderAccount.getAccountId()).build());
        depositMoney(
            AccountDepositRequestDto.builder().accountId(receiverAccount.getAccountId()).build());

        return senderAccount.getBalance();
    }

    @Transactional
    public Long depositMoney(AccountDepositRequestDto requestDto) {
        Account account = accountRepository.findByAccountId(requestDto.getAccountId())
            .orElseThrow(IllegalArgumentException::new);

        account.depositMoney(requestDto.getAmount());

        return account.getAccountId();
    }

    @Transactional
    public Long withdrawMoney(AccountWithdrawRequestDto requestDto) {
        Account account = accountRepository.findByAccountId(requestDto.getAccountId())
            .orElseThrow(IllegalArgumentException::new);

        account.withdrawMoney(requestDto.getAmount());

        return account.getBalance();
    }
}
