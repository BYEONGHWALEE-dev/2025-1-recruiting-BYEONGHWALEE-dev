package com.yourssu.application.dao;

import com.yourssu.application.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

    Article findByTitle(String title);
}
