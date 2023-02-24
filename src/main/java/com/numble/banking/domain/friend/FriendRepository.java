package com.numble.banking.domain.friend;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<Friend, FriendRelationKey> {

    @Query("SELECT F FROM Friend F WHERE F.friendRelationKey.user.userId = :userId")
    List<Friend> findFriendsByUserId(@Param("userId") Long userId);

}
