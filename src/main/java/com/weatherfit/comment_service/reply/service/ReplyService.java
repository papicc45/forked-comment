package com.weatherfit.comment_service.reply.service;

import com.weatherfit.comment_service.reply.dto.ReplyRequestDTO;
import com.weatherfit.comment_service.reply.dto.ReplyResponseDTO;
import reactor.core.publisher.Mono;


public interface ReplyService {

    Mono<ReplyResponseDTO> writeReply(ReplyRequestDTO replyRequestDTO);;

    Mono<Void> deleteReply(Long replyId);
}
