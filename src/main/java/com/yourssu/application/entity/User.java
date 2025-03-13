package com.yourssu.application.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false, length = 70)
    private String password;

    @Email(message = "옳바른 이메일 양식이 아닙니다.")
    @Column(name = "email", nullable = false)
    private String email;

    @OneToMany(mappedBy = "user",
                fetch = FetchType.LAZY,
                cascade = CascadeType.ALL)
    private List<Article> articles;

    @OneToMany(mappedBy = "user",
                fetch = FetchType.LAZY,
                cascade = CascadeType.ALL)
    private List<Comment> comments;

    // constructors
    public User() {}

    public User(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public void addArticle(Article article) {
        if(this.articles == null){
            this.articles = new ArrayList<>();
        }
        this.articles.add(article);
    }

    public void addComment(Comment comment) {
        if(this.comments == null){
            this.comments = new ArrayList<>();
        }
        this.comments.add(comment);
    }

}
