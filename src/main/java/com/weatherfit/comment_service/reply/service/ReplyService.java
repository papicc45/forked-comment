package com.weatherfit.comment_service.reply.service;


import com.weatherfit.comment_service.reply.dto.ReplyRequestDTO;
import com.weatherfit.comment_service.reply.dto.ReplyResponseDTO;
import com.weatherfit.comment_service.reply.entity.Reply;
import com.weatherfit.comment_service.reply.mapper.ReplyMapper;
import com.weatherfit.comment_service.reply.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyMapper replyMapper;
    private final ReplyRepository replyRepository;

    public Mono<ReplyResponseDTO> writeReply(ReplyRequestDTO replyRequestDTO) {
        Reply reply = replyMapper.DTOToReply(replyRequestDTO);

        return replyRepository
                .save(reply)
                .map(replyMapper::replyToDTO);
    }
    //    public ReplyResponseDTO writeReply(ReplyRequestDTO replyRequestDTO) {
    //        Optional<Comment> findComment = commentRepository.findById(replyRequestDTO.getCommentId());
    //        Comment comment = findComment.get();
    //        Reply reply = replyMapper.DTOToReply(replyRequestDTO);
    //        reply.setComment(comment);
    //        Reply insertReply = replyRepository.save(reply);
    //        return replyMapper.replyToDTO(insertReply);
    //    }

    public Mono<Void> deleteReply(Long replyId) {
        return replyRepository.findById(replyId)
                .switchIfEmpty(
                        Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 Id에 맞는 대댓글이 없음"))
                )
                .flatMap(reply -> {
                    reply.setStatus(0);
                    return replyRepository.save(reply);
                })
                .then();
    }
    //    public Boolean removeReply(int replyId) {
    //        Optional<Reply> findReply = replyRepository.findById(replyId);
    //        Reply reply = findReply.get();
    //        reply.setStatus(0);
    //        Reply result = replyRepository.save(reply);
    //        return result == null ? false : true;
    //    }
}
