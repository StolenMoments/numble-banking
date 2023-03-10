package com.numble.banking.service;

import com.numble.banking.domain.account.Account;
import com.numble.banking.domain.account.AccountRepository;
import com.numble.banking.domain.friend.FriendRelationKey;
import com.numble.banking.domain.friend.FriendRepository;
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

    private final FriendRepository friendRepository;

    private final AccountAlarmService alarmService;

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
    public Long transferMoney(AccountTransferRequestDto requestDto) throws InterruptedException {
        User sender = userRepository.findByLoginId(requestDto.getLoginId())
            .orElseThrow(IllegalArgumentException::new);

        User receiver = userRepository.findByLoginId(requestDto.getReceiverLoginId())
            .orElseThrow(IllegalArgumentException::new);

        FriendRelationKey key = new FriendRelationKey(sender, receiver.getUserId());

        if (friendRepository.findById(key).isEmpty()) {
            throw new IllegalArgumentException("친구가 아닙니다.");
        }

        Account senderAccount = accountRepository.findByUser(sender)
            .orElseThrow(IllegalArgumentException::new);
        Account receiverAccount = accountRepository.findByUser(receiver)
            .orElseThrow(IllegalArgumentException::new);

        withdrawMoney(
            AccountWithdrawRequestDto.builder()
                .amount(requestDto.getAmount())
                .accountId(senderAccount.getAccountId())
                .build());
        depositMoney(
            AccountDepositRequestDto.builder()
                .amount(requestDto.getAmount())
                .accountId(receiverAccount.getAccountId())
                .build());

        alarmService.callAlarmService(receiver.getLoginId(), "계좌 이체 완료");

        return senderAccount.getBalance();
    }

    @Transactional
    public Long depositMoney(AccountDepositRequestDto requestDto) {
        Account account = accountRepository.findByAccountId(requestDto.getAccountId())
            .orElseThrow(IllegalArgumentException::new);

        account.depositMoney(requestDto.getAmount());

        return account.getBalance();
    }

    @Transactional
    public Long withdrawMoney(AccountWithdrawRequestDto requestDto) {
        Account account = accountRepository.findByAccountId(requestDto.getAccountId())
            .orElseThrow(IllegalArgumentException::new);

        account.withdrawMoney(requestDto.getAmount());

        return account.getBalance();
    }

    @Transactional
    public Long getBalance(Long accountId) {
        return accountRepository.findByAccountId(accountId)
            .orElseThrow(IllegalArgumentException::new).getBalance();
    }
}
