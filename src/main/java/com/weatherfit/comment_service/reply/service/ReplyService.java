package com.weatherfit.comment_service.reply.service;


import com.weatherfit.comment_service.reply.dto.ReplyRequestDTO;
import com.weatherfit.comment_service.reply.dto.ReplyResponseDTO;
import com.weatherfit.comment_service.reply.entity.Reply;
import com.weatherfit.comment_service.reply.mapper.ReplyMapper;
import com.weatherfit.comment_service.reply.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
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
}
