package com.camon.dailylog.articles.service;

import com.camon.dailylog.articles.model.Article;
import com.camon.dailylog.articles.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by jooyong on 2015-11-18.
 */
@Service
@Transactional
@Slf4j
public class ArticleService {

    @Autowired
    private ArticleRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    public Article add(Article article) {
        Date now = new Date();
        article.setCreated(now);
        return repository.save(article);
    }

    public Article findById(Long id) {
        return repository.findOne(id);
    }

    public List<Article> findAll() {
        return repository.findAll();
    }

    public Article modify(Article article) {
        Date now = new Date();
        article.setUpdated(now);
        return repository.save(article);
    }

    public void remove(Long id) {
        repository.delete(id);
    }
}
