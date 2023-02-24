package com.numble.banking.domain.friend;


import com.numble.banking.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendRelationKey implements Serializable {

    @Serial
    private static final long serialVersionUID = 591110102552547473L;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    @ToString.Exclude
    private User user;

    @Column(name = "FRIEND_ID")
    private Long friendId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FriendRelationKey that = (FriendRelationKey) o;

        if (!Objects.equals(user, that.user)) {
            return false;
        }
        return Objects.equals(friendId, that.friendId);
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (friendId != null ? friendId.hashCode() : 0);
        return result;
    }
}
