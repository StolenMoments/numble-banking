package com.numble.banking.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.numble.banking.domain.account.Account;
import com.numble.banking.domain.account.AccountRepository;
import com.numble.banking.domain.friend.Friend;
import com.numble.banking.domain.friend.FriendRelationKey;
import com.numble.banking.domain.friend.FriendRepository;
import com.numble.banking.domain.user.User;
import com.numble.banking.domain.user.UserRepository;
import com.numble.banking.dto.AccountCreateRequestDto;
import com.numble.banking.dto.AccountDepositRequestDto;
import com.numble.banking.dto.AccountTransferRequestDto;
import com.numble.banking.dto.AccountWithdrawRequestDto;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = {"classpath:application-test.yml"})
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FriendRepository friendRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void testCreateAccount() {
        // given
        long accountId = 1L;

        AccountCreateRequestDto requestDto = AccountCreateRequestDto.builder()
            .loginId("user01")
            .amount(1000L).build();

        User user = User.builder()
            .userId(1L)
            .loginId("user01")
            .build();

        Account account = Account.builder()
            .accountId(accountId)
            .user(user)
            .balance(0L)
            .build();

        when(userRepository.findByLoginId(requestDto.getLoginId())).thenReturn(
            Optional.of(user));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        // when
        Long expectedAccountId = accountService.createAccount(requestDto);

        // then
        assertThat(expectedAccountId).isEqualTo(accountId);

    }

    @Test
    void testDepositMoney() {
        // given
        Long accountId = 1L;
        Long initBalance = 0L;
        Long amount = 100L;

        Account account = Account.builder()
            .accountId(accountId)
            .balance(initBalance)
            .user(User.builder().userId(1L).build())
            .build();

        when(accountRepository.findByAccountId(accountId)).thenReturn(Optional.of(account));

        // when
        Long updatedBalance = accountService.depositMoney(
            AccountDepositRequestDto.builder()
                .accountId(accountId)
                .amount(amount)
                .build());

        // then
        assertThat(updatedBalance).isEqualTo(amount + initBalance);
    }

    @Test
    void testWithdrawMoney() {
        // given
        Long accountId = 1L;
        Long initBalance = 10000L;
        Long amount = 5000L;
        Account account = Account.builder()
            .accountId(accountId)
            .balance(initBalance)
            .user(User.builder().userId(1L).build())
            .build();

        when(accountRepository.findByAccountId(accountId)).thenReturn(Optional.of(account));

        // when
        Long updatedBalance = accountService.withdrawMoney(
            AccountWithdrawRequestDto.builder()
                .accountId(accountId)
                .amount(amount)
                .build());

        // then
        assertThat(updatedBalance).isEqualTo(initBalance - amount);
    }

    @Test
    void testGetBalance() {
        // given
        Long accountId = 1L;
        Long balance = 100L;
        Account account = Account.builder()
            .accountId(accountId)
            .balance(balance)
            .user(User.builder().userId(1L).build())
            .build();

        when(accountRepository.findByAccountId(accountId)).thenReturn(Optional.of(account));

        // when
        Long result = accountService.getBalance(accountId);

        // then
        assertThat(result).isEqualTo(balance);
    }

    @Test
    void testTransferMoney() {
        // given
        long senderBalance = 10000L;
        long receiverBalance = 5000L;
        long amount = 3000L;

        User sender = User.builder()
            .loginId("sender")
            .build();
        User receiver = User.builder()
            .loginId("receiver")
            .build();
        Account senderAccount = Account.builder()
            .user(sender)
            .balance(senderBalance)
            .accountId(1L)
            .build();
        Account receiverAccount = Account.builder()
            .user(receiver)
            .balance(receiverBalance)
            .accountId(2L)
            .build();
        AccountTransferRequestDto requestDto = AccountTransferRequestDto.builder()
            .loginId("sender")
            .receiverLoginId("receiver")
            .amount(amount)
            .build();

        FriendRelationKey key = new FriendRelationKey(sender, receiver.getUserId());

        when(friendRepository.findById(key)).thenReturn(
            Optional.of(new Friend(sender, receiver.getUserId())));
        when(userRepository.findByLoginId("sender")).thenReturn(Optional.of(sender));
        when(userRepository.findByLoginId("receiver")).thenReturn(Optional.of(receiver));
        when(accountRepository.findByUser(sender)).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findByUser(receiver)).thenReturn(Optional.of(receiverAccount));
        when(accountRepository.findByAccountId(1L)).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findByAccountId(2L)).thenReturn(Optional.of(receiverAccount));

        // when
        Long balance = accountService.transferMoney(requestDto);

        // then
        assertThat(balance).isEqualTo(senderBalance - amount);
    }
}