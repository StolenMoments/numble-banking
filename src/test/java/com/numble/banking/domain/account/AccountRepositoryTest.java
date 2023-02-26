package com.numble.banking.domain.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.numble.banking.domain.user.User;
import com.numble.banking.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    User user01 = null;
    User user02 = null;

    @BeforeEach
    public void setup() {
        user01 = User.builder().loginId("user01").build();
        user02 = User.builder().loginId("user02").build();
        user01 = userRepository.save(user01);
        user02 = userRepository.save(user02);
    }

    @Test
    @DisplayName("사용자 계좌 저장")
    @Order(1)
    void testSave() {
        // given
        Account account = Account.builder()
            .user(user01)
            .balance(0L)
            .build();

        // when
        accountRepository.save(account);

        // then
        Account savedAccount = accountRepository.findByUser(user01).orElseThrow();
        assertThat(savedAccount).isEqualTo(account);
    }

    @Test
    @DisplayName("존재하지 않는 사용자의 계좌를 저장하는 경우")
    @Order(2)
    void testSaveWhenNoneUser() {
        // given
        User noneUser = User.builder().userId(1000000L).loginId("noneUser").build();

        Account account = Account.builder()
            .user(noneUser)
            .balance(0L)
            .build();

        // when & then
        assertThatThrownBy(() -> accountRepository.save(account))
            .hasMessageContaining("constraint");
    }

    @Test
    @DisplayName("계좌 입금")
    @Order(3)
    void testUpdateDeposit() {
        // given
        long amount = 10000L;
        long initBalance = 0L;

        Account account = Account.builder()
            .user(user01)
            .balance(initBalance)
            .build();

        accountRepository.save(account);

        // when
        account.depositMoney(amount);
        Account updatedAccount = accountRepository.findByUser(user01).orElseThrow();

        // then
        assertThat(updatedAccount.getBalance()).isEqualTo(initBalance + amount);
    }

    @Test
    @DisplayName("계좌 출금")
    @Order(3)
    void testUpdateWithdraw() {
        // given
        long amount = 10000L;
        long initBalance = 50000L;

        Account account = Account.builder()
            .user(user01)
            .balance(initBalance)
            .build();

        accountRepository.save(account);

        // when
        account.withdrawMoney(amount);
        Account updatedAccount = accountRepository.findByUser(user01).orElseThrow();

        // then
        assertThat(updatedAccount.getBalance()).isEqualTo(initBalance - amount);
    }
}
