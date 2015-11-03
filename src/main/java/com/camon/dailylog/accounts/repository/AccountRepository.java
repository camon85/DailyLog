package com.camon.dailylog.accounts.repository;


import com.camon.dailylog.accounts.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jooyong on 2015-11-02.
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByUsername(String username);
}
