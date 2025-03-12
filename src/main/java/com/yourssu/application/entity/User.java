package com.yourssu.application.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, length = 70)
    private String password;

    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "user",
                fetch = FetchType.LAZY,
                cascade = CascadeType.ALL)
    private List<Article> articles;

    @OneToMany(mappedBy = "user",
                fetch = FetchType.LAZY,
                cascade = CascadeType.ALL)
    private List<Comment> comments;


    void addArticle(Article article) {
        if(this.articles == null){
            this.articles = new ArrayList<>();
        }
        this.articles.add(article);
    }

    void addComment(Comment comment) {
        if(this.comments == null){
            this.comments = new ArrayList<>();
        }
        this.comments.add(comment);
    }
}
