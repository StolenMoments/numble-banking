package com.numble.banking.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.numble.banking.domain.user.User;
import com.numble.banking.domain.user.UserRepository;
import com.numble.banking.dto.UserCreateRequestDto;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("사용자 생성")
    void testCreateUser() {
        // given
        String loginId = "testUser";
        String password = "password";
        String userName = "Test User";

        UserCreateRequestDto requestDto = UserCreateRequestDto.builder()
            .loginId(loginId)
            .password(password)
            .userName(userName)
            .build();

        User user = requestDto.toEntity();
        user.setUserId(1L);
        user.setPassword("encodedPassword");

        when(userRepository.findByLoginId(loginId)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        // when
        Long createdUserId = userService.createUser(requestDto);

        // then
        assertThat(createdUserId).isEqualTo(1L);
    }

    @Test
    @DisplayName("생성하려는 아이디가 존재하는 경우")
    void testCreateUserWithExistingLoginId() {
        // given
        String loginId = "test";
        String password = "password";
        String userName = "name";

        UserCreateRequestDto requestDto = UserCreateRequestDto.builder()
            .loginId(loginId)
            .password(password)
            .userName(userName)
            .build();

        User existingUser = requestDto.toEntity();

        when(userRepository.findByLoginId(loginId)).thenReturn(Optional.of(existingUser));

        // when
        Long createdUserId = userService.createUser(requestDto);

        // then
        assertThat(createdUserId).isEqualTo(-1L);
    }
}