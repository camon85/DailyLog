package com.camon.dailylog.accounts.domain;

import com.camon.dailylog.articles.model.Article;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by jooyong on 2015-11-02.
 */
@Entity
@Data
public class Account {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private String email;

    private String fullName;

    @Temporal(TemporalType.TIMESTAMP)
    private Date joined;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    private boolean admin;

    @OneToMany(mappedBy = "account")
    @JsonIgnore
    private List<Article> articles;
}
