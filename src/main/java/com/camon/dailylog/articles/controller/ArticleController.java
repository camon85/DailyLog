package com.camon.dailylog.articles.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by jooyong on 2015-11-18.
 */

@Controller
@RequestMapping("/articles")
@Slf4j
public class ArticleController {

    @RequestMapping(method = GET)
    public String list() {
        return "articles/list";
    }
}
