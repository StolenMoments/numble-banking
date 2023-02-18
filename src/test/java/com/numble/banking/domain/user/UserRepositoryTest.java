package com.numble.banking.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("사용자 DB 저장")
    void testSaveUser() {
        // given
        User user = User.builder().userName("TestUser").password("1234").loginId("LOGIN_ID")
            .build();

        // when
        repository.save(user);

        // then
        User savedUser = repository.findByLoginId("LOGIN_ID");
        assertThat(savedUser).isEqualTo(user);
    }
}