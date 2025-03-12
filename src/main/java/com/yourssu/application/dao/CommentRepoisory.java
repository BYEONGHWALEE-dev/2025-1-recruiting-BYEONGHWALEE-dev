package com.yourssu.application.dao;

import com.yourssu.application.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepoisory extends JpaRepository<Comment, Integer> {
}
