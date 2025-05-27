package com.weatherfit.comment_service.reply.repository;

import com.weatherfit.comment_service.reply.entity.Reply;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ReplyRepository
        extends ReactiveCrudRepository<Reply, Integer> {
    Flux<Reply> findByCommentId(Integer commentId);
}