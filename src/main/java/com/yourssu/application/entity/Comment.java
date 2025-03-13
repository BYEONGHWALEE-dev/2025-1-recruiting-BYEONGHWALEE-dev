package com.yourssu.application.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "comments")
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentId;

    @NotBlank // "", " ", "NULL" 값을 방지하기 위함
    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY,
    cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "article_id")
    private Article article;

    // constructors
    public Comment() {}

    public Comment(String content) {
        this.content = content;
    }
}
