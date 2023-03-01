package com.numble.banking.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.numble.banking.domain.account.AccountRepository;
import com.numble.banking.dto.AccountCreateRequestDto;
import com.numble.banking.dto.AccountDepositRequestDto;
import com.numble.banking.dto.AccountTransferRequestDto;
import com.numble.banking.dto.FriendAddRequestDto;
import com.numble.banking.dto.UserCreateRequestDto;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AccountConcurrencyTest {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FriendService friendService;

    long senderUserId;
    long receiverUserId;
    long senderAccountId;
    long receiverAccountId;

    @BeforeEach
    void setup() {
        UserCreateRequestDto userCreateRequestDto1 = UserCreateRequestDto.builder()
            .loginId("sender").password("1234").userName("john").build();
        senderUserId = userService.createUser(userCreateRequestDto1);

        UserCreateRequestDto userCreateRequestDto2 = UserCreateRequestDto.builder()
            .loginId("receiver").password("1234").userName("steven").build();
        receiverUserId = userService.createUser(userCreateRequestDto2);

        AccountCreateRequestDto accountCreateRequestDto1 = AccountCreateRequestDto.builder()
            .loginId("sender").amount(100000L).build();
        senderAccountId = accountService.createAccount(accountCreateRequestDto1);

        AccountCreateRequestDto accountCreateRequestDto2 = AccountCreateRequestDto.builder()
            .loginId("receiver").amount(0L).build();
        receiverAccountId = accountService.createAccount(accountCreateRequestDto2);

        friendService.addFriend(FriendAddRequestDto.builder()
            .fromLoginId("sender")
            .toLoginId("receiver")
            .build());
    }

    @AfterEach
    void cleanUp() {
        accountRepository.deleteAll();
    }

    @Test
    void depositTestWithConcurrency() throws InterruptedException {

        long amount = 500L;
        int numberOfThreads = 10;

        ExecutorService exeService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            exeService.execute(() -> {
                accountService.depositMoney(
                    AccountDepositRequestDto.builder()
                        .accountId(senderAccountId)
                        .amount(amount)
                        .build());

                latch.countDown();
            });
        }
        latch.await();

        Long balance = accountService.getBalance(senderAccountId);
        assertThat(balance).isEqualTo(
            10000L + (amount * numberOfThreads)
        );
    }

    @Test
    void transferTestWithConcurrency() throws InterruptedException {

        long amount = 500L;
        int numberOfThreads = 10;
        int executeCnt = numberOfThreads * 5;

        ExecutorService exeService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(executeCnt);
        for (int i = 0; i < executeCnt; i++) {
            exeService.execute(() -> {
                try {
                    accountService.transferMoney(
                        AccountTransferRequestDto.builder()
                            .senderAccountId(senderAccountId)
                            .receiverAccountId(receiverAccountId)
                            .receiverLoginId("receiver")
                            .loginId("sender")
                            .amount(amount)
                            .build());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        Long senderBalance = accountService.getBalance(senderAccountId);
        Long receiverBalance = accountService.getBalance(receiverAccountId);
        assertThat(senderBalance + receiverBalance).isEqualTo(100000L);
        assertThat(receiverBalance).isEqualTo(amount * executeCnt);
        assertThat(senderBalance).isEqualTo(100000L - (amount * executeCnt));
    }
}