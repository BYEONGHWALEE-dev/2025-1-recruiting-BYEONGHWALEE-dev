package com.yourssu.application.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "articles")
@Getter
@Setter
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int articleId;

    @NotBlank // "", " ", "NULL" 값을 방지하기 위함
    @Column(nullable = false)
    private String title;

    @NotBlank // "", " ", "NULL" 값을 방지하기 위함
    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "article",
    fetch = FetchType.LAZY,
    cascade = CascadeType.ALL)
    private List<Comment> comments;


    // constructors
    public Article() {}

    public Article(String title, String content) {
        this.title = title;
        this.content = content;
    }

    void addComment(Comment comment) {
        if (this.comments == null) {
            this.comments = new ArrayList<>();
        }
        this.comments.add(comment);
    }

}
