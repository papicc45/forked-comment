package com.weatherfit.comment_service.service;

import com.weatherfit.comment_service.common.mapper.CommentMapper;
import com.weatherfit.comment_service.dto.CommentRepsonseDTO;
import com.weatherfit.comment_service.dto.CommentRequestDTO;
import com.weatherfit.comment_service.dto.ReplyRequestDTO;
import com.weatherfit.comment_service.dto.ReplyResponseDTO;
import com.weatherfit.comment_service.entity.Comment;
import com.weatherfit.comment_service.entity.Reply;
import com.weatherfit.comment_service.repository.CommentRepository;
import com.weatherfit.comment_service.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;

    private final CommentMapper commentMapper;

    public CommentRepsonseDTO writeComment(CommentRequestDTO commentRequestDTO) {
        Comment comment = commentMapper.DTOToComment(commentRequestDTO);
        Comment insertComment = commentRepository.save(comment);
        return commentMapper.commentToDTO(insertComment);
    }

    public ReplyResponseDTO writeReply(ReplyRequestDTO replyRequestDTO) {
        Reply reply = commentMapper.DTOToReply(replyRequestDTO);
        Reply insertReply = replyRepository.save(reply);
        return commentMapper.replyToDTO(insertReply);
    }
}
