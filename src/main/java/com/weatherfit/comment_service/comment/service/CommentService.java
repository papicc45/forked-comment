package com.weatherfit.comment_service.comment.service;

//import com.weatherfit.comment_service.common.mapper.ReplyMapper;
import com.weatherfit.comment_service.comment.dto.CommentRequestDTO;
import com.weatherfit.comment_service.comment.dto.CommentResponseDTO;
import com.weatherfit.comment_service.comment.entity.Comment;
import com.weatherfit.comment_service.comment.mapper.CommentMapper;
import com.weatherfit.comment_service.comment.repository.CommentRepository;
import com.weatherfit.comment_service.reply.mapper.ReplyMapper;
import com.weatherfit.comment_service.reply.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;
//    private final ReplyMapper replyMapper;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final ReplyMapper replyMapper;

    public Mono<CommentResponseDTO> writeComment(CommentRequestDTO dto) {
        Comment comment = commentMapper.DTOToComment(dto);

        return commentRepository
                .save(comment)                                // Mono<Comment>
                .map(commentMapper::commentToDTO);            // Mono<CommentResponseDTO>
    }

    public Flux<CommentResponseDTO> getCommentsByBoardId(int boardId) {
        return commentRepository.findByBoardId(boardId)
                .flatMap(comment ->
                        replyRepository.findByCommentId(comment.getId())
                                .collectList()
                                .map(replies -> {
                                    var dto = commentMapper.commentToDTO(comment);
                                    dto.setReplyList(replyMapper.replyListToDTOs(replies));
                                    return dto;
                                }));
    }
//    public ReplyResponseDTO writeReply(ReplyRequestDTO replyRequestDTO) {
//        Optional<Comment> findComment = commentRepository.findById(replyRequestDTO.getCommentId());
//        Comment comment = findComment.get();
//        Reply reply = replyMapper.DTOToReply(replyRequestDTO);
//        reply.setComment(comment);
//        Reply insertReply = replyRepository.save(reply);
//        return replyMapper.replyToDTO(insertReply);
//    }
//
//    public List<CommentResponseDTO> getCommentsByBoard(int boardId) {
//        List<Comment> comments = commentRepository.findByBoardId(boardId);
//
//        comments.forEach(comment -> comment.getReplyList().size());
//        return commentMapper.commentsToDTOList(comments);
//    }
//
//    public Boolean removeComment(int commentId) {
//        Optional<Comment> findComment = commentRepository.findById(commentId);
//        Comment comment = findComment.get();
//        comment.setStatus(0);
//        Comment result = commentRepository.save(comment);
//        return result == null ? false : true;
//    }
//
//    public Boolean removeReply(int replyId) {
//        Optional<Reply> findReply = replyRepository.findById(replyId);
//        Reply reply = findReply.get();
//        reply.setStatus(0);
//        Reply result = replyRepository.save(reply);
//        return result == null ? false : true;
//    }
//
//    public Boolean modifyComment(CommentRequestDTO commentRequestDTO) {
//        Optional<Comment> findComment = commentRepository.findById(commentRequestDTO.getId());
//        Comment comment = findComment.get();
//        comment.setContent(commentRequestDTO.getContent());
//        Comment result = commentRepository.save(comment);
//        return result == null ? false : true;
//    }
//
//    public Boolean modifyReply(ReplyRequestDTO replyRequestDTO) {
//        Optional<Reply> findReply = replyRepository.findById(replyRequestDTO.getId());
//        Reply reply = findReply.get();
//        reply.setContent(replyRequestDTO.getContent());
//        Reply result = replyRepository.save(reply);
//        return result == null ? false : true;
//    }
}
