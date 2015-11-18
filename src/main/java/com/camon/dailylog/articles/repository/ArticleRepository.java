package com.camon.dailylog.articles.repository;

import com.camon.dailylog.articles.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jooyong on 2015-11-18.
 */
public interface ArticleRepository extends JpaRepository<Article, Long> {
}
