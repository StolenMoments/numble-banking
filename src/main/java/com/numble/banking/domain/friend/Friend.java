package com.numble.banking.domain.friend;

import com.numble.banking.domain.user.User;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "FRIEND")
public class Friend {

    @EmbeddedId
    private FriendRelationKey friendRelationKey;

    public Friend(User fromUser, Long toUserId) {
        this.friendRelationKey = new FriendRelationKey(fromUser, toUserId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Friend friend = (Friend) o;
        return friendRelationKey.equals(friend.friendRelationKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(friendRelationKey);
    }
}
