package com.numble.banking.domain.account;

import com.numble.banking.domain.user.User;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUser(User user);
    Optional<Account> findByUser_LoginId(String loginId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Account> findByAccountId(Long accountId);

}
