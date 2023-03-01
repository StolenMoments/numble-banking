package com.numble.banking.service;

import com.numble.banking.domain.friend.Friend;
import com.numble.banking.domain.friend.FriendRepository;
import com.numble.banking.domain.user.User;
import com.numble.banking.domain.user.UserRepository;
import com.numble.banking.dto.FriendAddRequestDto;
import com.numble.banking.dto.FriendResponseDto;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<FriendResponseDto> getFriends(String loginId) {
        User user = userRepository.findByLoginId(loginId)
            .orElseThrow(IllegalArgumentException::new);

        List<Friend> friends = friendRepository.findFriendsByUserId(user.getUserId());

        List<FriendResponseDto> dtos = new ArrayList<>();

        for (Friend friend : friends) {
            User friendUser = userRepository.findById(friend.getFriendRelationKey().getFriendId())
                .orElse(null);
            if (friendUser == null) {
                continue;
            }

            dtos.add(new FriendResponseDto(friendUser));
        }

        return dtos;
    }

    @Transactional
    public Long addFriend(FriendAddRequestDto requestDto) {
        User fromUser = userRepository.findByLoginId(requestDto.getFromLoginId()).orElseThrow(IllegalArgumentException::new);
        User toUser = userRepository.findByLoginId(requestDto.getToLoginId()).orElseThrow(IllegalArgumentException::new);

        friendRepository.save(new Friend(fromUser, toUser.getUserId()));
        friendRepository.save(new Friend(toUser, fromUser.getUserId()));

        return fromUser.getUserId();
    }
}
