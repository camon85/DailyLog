package com.camon.dailylog.accounts.service;


import com.camon.dailylog.accounts.domain.Account;
import com.camon.dailylog.accounts.domain.AccountDto;
import com.camon.dailylog.accounts.exception.UserDuplicatedException;
import com.camon.dailylog.accounts.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by jooyong on 2015-11-02.
 */
@Service
@Transactional
@Slf4j
public class AccountService {

//    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AccountRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    public Account createAccount(AccountDto.Create dto) {
        Account account = modelMapper.map(dto, Account.class);
        String username = dto.getUsername();

        if (repository.findByUsername(username) != null) {
            log.error("UserDuplicatedException: {}", username);
            throw new UserDuplicatedException(username);
        }

        Date now = new Date();
        account.setJoined(now);
        account.setUpdated(now);

        return repository.save(account);
    }

    public Account updateAccount(Account account, AccountDto.Update updataDto) {
        account.setPassword(updataDto.getPassword());
        account.setFullName(updataDto.getFullName());
        return account;
    }

    public Account findAccount(Long id) {
        Account account = repository.findOne(id);
        if (account == null) {
            throw new AccountNotFoundException(id);
        }
        return account;
    }
}
