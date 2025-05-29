package com.weatherfit.comment_service.comment.service;

import com.weatherfit.comment_service.comment.dto.CommentRequestDTO;
import com.weatherfit.comment_service.comment.dto.CommentResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommentService {
    Mono<CommentResponseDTO> writeComment(Mono<CommentRequestDTO> commentRequestDTO);

    Flux<CommentResponseDTO> getCommentsByBoardId(Long boardId);

    Mono<Void> deleteComment(Long commentId);
}
