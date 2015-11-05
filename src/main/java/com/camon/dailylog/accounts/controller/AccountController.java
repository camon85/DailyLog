package com.camon.dailylog.accounts.controller;

import com.camon.dailylog.accounts.domain.Account;
import com.camon.dailylog.accounts.domain.AccountDto;
import com.camon.dailylog.accounts.exception.UserDuplicatedException;
import com.camon.dailylog.accounts.repository.AccountRepository;
import com.camon.dailylog.accounts.service.AccountNotFoundException;
import com.camon.dailylog.accounts.service.AccountService;
import com.camon.dailylog.commons.ErrorResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jooyong on 2015-11-02.
 */
@RestController
public class AccountController {

    @Autowired
    private AccountService service;

    @Autowired
    private AccountRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @ExceptionHandler(UserDuplicatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserDuplicatedException(UserDuplicatedException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("[" + e.getUsername() + "]" + "중복된 username 입니다.");
        errorResponse.setCode("duplicated.username.exception");
        return errorResponse;
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAccountNotFoundException(AccountNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("[" + e.getId() + "]" + "해당되는 id가 존재하지 않습니다.");
        errorResponse.setCode("account.nott.found.exception");
        return errorResponse;
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    public ResponseEntity createAccount(@RequestBody @Valid AccountDto.Create create, BindingResult result) {
        // TODO validator로 빼버릴까
        if (result.hasErrors()) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("잘못된 요청입니다.");
            errorResponse.setCode("bad.request");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        Account newAccount = service.createAccount(create);
        AccountDto.Response responseDto =
                modelMapper.map(newAccount, AccountDto.Response.class);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public PageImpl<AccountDto.Response> findAll(Pageable pageable) {
        Page<Account> page = repository.findAll(pageable);
        List<AccountDto.Response> content = page.getContent().parallelStream().
                map(account -> modelMapper.map(account, AccountDto.Response.class)).collect(Collectors.toList());
        return new PageImpl<>(content, pageable, page.getTotalElements());
    }

    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public AccountDto.Response findById(@PathVariable Long id) {
        Account account = repository.findOne(id);
        AccountDto.Response result = modelMapper.map(account, AccountDto.Response.class);
        return result;
    }

    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity updateAccount(@PathVariable Long id, @RequestBody @Valid AccountDto.Update updateDto, BindingResult result) {

        if (result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Account account = repository.findOne(id);
        Account updatedAccount = service.updateAccount(account, updateDto);
        return new ResponseEntity<>(modelMapper.map(updatedAccount, AccountDto.Response.class), HttpStatus.OK);
    }
}
