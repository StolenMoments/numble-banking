package com.numble.banking.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.numble.banking.domain.friend.Friend;
import com.numble.banking.domain.friend.FriendRepository;
import com.numble.banking.domain.user.User;
import com.numble.banking.domain.user.UserRepository;
import com.numble.banking.dto.FriendAddRequestDto;
import com.numble.banking.dto.FriendResponseDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yml"})
class FriendServiceTest {

    @Mock
    private FriendRepository friendRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FriendService friendService;

    @Test
    void testGetFriends() {
        // given
        String loginId = "testUser";

        User user = User.builder().userId(1L).build();

        List<Friend> friends = new ArrayList<>();
        friends.add(new Friend(user, 2L));
        friends.add(new Friend(user, 3L));

        User friendUser1 = User.builder().userId(2L).build();
        User friendUser2 = User.builder().userId(3L).build();

        when(userRepository.findByLoginId(loginId)).thenReturn(Optional.of(user));
        when(friendRepository.findFriendsByUserId(user.getUserId())).thenReturn(friends);
        when(userRepository.findById(2L)).thenReturn(Optional.of(friendUser1));
        when(userRepository.findById(3L)).thenReturn(Optional.of(friendUser2));

        // when
        List<FriendResponseDto> friendResponseDtos = friendService.getFriends(loginId);


        // then
        assertThat(friendResponseDtos).hasSize(2);
        assertThat(friendResponseDtos.get(0).getUserId()).isEqualTo(2L);
        assertThat(friendResponseDtos.get(1).getUserId()).isEqualTo(3L);
    }

    @Test
    void testAddFriend() {
        // given
        String fromLoginId = "fromUser";
        String toLoginId = "toUser";

        Long fromUserId = 1L;
        Long toUserId = 2L;

        User fromUser = User.builder().userId(fromUserId).build();
        User toUser = User.builder().userId(toUserId).build();

        FriendAddRequestDto requestDto = new FriendAddRequestDto(fromLoginId, toLoginId);

        when(userRepository.findByLoginId(fromLoginId)).thenReturn(Optional.of(fromUser));
        when(userRepository.findByLoginId(toLoginId)).thenReturn(Optional.of(toUser));

        // when
        Long result = friendService.addFriend(requestDto);

        // then
        assertThat(result).isEqualTo(fromUserId);
    }
}