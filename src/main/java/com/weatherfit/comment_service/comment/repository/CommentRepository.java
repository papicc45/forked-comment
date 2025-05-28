package com.weatherfit.comment_service.comment.repository;

import com.weatherfit.comment_service.comment.entity.Comment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface CommentRepository
        extends ReactiveCrudRepository<Comment, Long> {

    // boardId 로 Comment 리스트 조회
    Flux<Comment> findByBoardId(Long boardId);

}