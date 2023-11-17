package com.weatherfit.comment_service.repository;

import com.weatherfit.comment_service.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
