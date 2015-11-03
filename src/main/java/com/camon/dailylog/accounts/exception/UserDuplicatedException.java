package com.camon.dailylog.accounts.exception;

import lombok.Getter;

/**
 * Created by jooyong on 2015-11-03.
 */
@Getter
public class UserDuplicatedException extends RuntimeException {

    private String username;

    public UserDuplicatedException(String username) {
        this.username = username;
    }
}
