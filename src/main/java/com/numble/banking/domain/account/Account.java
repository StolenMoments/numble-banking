package com.numble.banking.domain.account;

import com.numble.banking.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.Hibernate;

@Getter
@ToString
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ACCOUNT")
public class Account implements Serializable {

    @Serial
    private static final long serialVersionUID = 491110102552547473L;
    @Id
    @Column(name = "ACCOUNT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    @ToString.Exclude
    private User user;

    @Column(name = "BALANCE")
    private Long balance;

    public void depositMoney(Long amount) {
        this.balance = this.balance + amount;
    }

    public void withdrawMoney(Long amount) {
        this.balance = this.balance - amount;
    }

    @Builder
    public Account(Long accountId, User user, Long balance) {
        this.accountId = accountId;
        this.user = user;
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(
            o)) {
            return false;
        }
        Account account = (Account) o;
        return accountId != null && Objects.equals(accountId, account.accountId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
