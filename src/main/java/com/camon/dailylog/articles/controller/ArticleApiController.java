package com.camon.dailylog.articles.controller;

import com.camon.dailylog.articles.model.Article;
import com.camon.dailylog.articles.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by jooyong on 2015-11-18.
 */
@RestController
@RequestMapping("/api/articles")
@Slf4j
public class ArticleApiController {

    @Autowired
    private ArticleService service;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(method = POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Article add(@RequestBody Article article) {
        Article created = service.add(article);
        return created;
    }

    @RequestMapping(method = GET)
    public List<Article> findAll() {
        List<Article> articles = service.findAll();
//        log.info("account id: ", articles.get(0).getAccount().getId());
        return articles;
    }

    @RequestMapping(value="/{id}", method = GET)
    @ResponseStatus(HttpStatus.OK)
    public String findById(@PathVariable Long id) {
        Article article = service.findById(id);
        return article.getContent();
    }

    @RequestMapping(value="/{id}", method = PUT)
    @ResponseStatus(HttpStatus.OK)
    public String modify(@PathVariable Long id, @RequestBody Article article) {
        service.modify(article);
        return null;
    }

    @RequestMapping(value="/{id}", method = DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String remove(@PathVariable Long id) {
        service.remove(id);
        return null;
    }



}
