package com.camon.dailylog.accounts.service;

import lombok.Getter;

/**
 * Created by jooyong on 2015-11-05.
 */
@Getter
public class AccountNotFoundException extends RuntimeException {
    private Long id;

    public AccountNotFoundException() {
    }

    public AccountNotFoundException(Long id) {
        this.id = id;
    }
}
