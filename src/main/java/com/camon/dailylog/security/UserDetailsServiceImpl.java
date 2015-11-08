package com.camon.dailylog.security;

import com.camon.dailylog.accounts.domain.Account;
import com.camon.dailylog.accounts.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by jooyong on 2015-11-08.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AccountRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = repository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException(username);
        }

        return new UserDetailsImpl(account);
    }
}
