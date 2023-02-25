package com.numble.banking.domain.friend;

import static org.assertj.core.api.Assertions.assertThat;

import com.numble.banking.domain.user.User;
import com.numble.banking.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FriendRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    private User user01;
    private User user02;

    @BeforeEach
    public void setup() {
        user01 = User.builder().userId(1L).userName("user01").loginId("user01").build();
        user02 = User.builder().userId(2L).userName("user02").loginId("user02").build();

        userRepository.save(user01);
        userRepository.save(user02);
    }

    @Test
    void testSaveFriend() {
        // given
        Friend friend = new Friend(user01, user02.getUserId());

        // when
        friendRepository.save(friend);

        //then
        Friend savedFriend01 = friendRepository.findById(
                new FriendRelationKey(user01, user02.getUserId()))
            .orElse(null);

        assertThat(savedFriend01).isEqualTo(friend);
    }
}