package com.camon.dailylog.articles.model;

import com.camon.dailylog.accounts.domain.Account;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by jooyong on 2015-11-18.
 */
@Entity
@Data
public class Article {
    @Id
    @GeneratedValue
    private Long id;

    private String content;

    private Date created;

    private Date updated;

    @JoinColumn(name = "account.id")
    @ManyToOne
    private Account account;

}
