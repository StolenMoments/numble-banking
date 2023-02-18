package com.numble.banking.service;

import com.numble.banking.domain.user.User;
import com.numble.banking.domain.user.UserRepository;
import com.numble.banking.dto.UserCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long createUser(UserCreateRequestDto requestDto) {
        if (userRepository.findByLoginId(requestDto.getLoginId()) != null) {
            // 임시 처리
            return -1L;
        }

        User user = new User(requestDto);
        return userRepository.save(user).getUserId();
    }
}
