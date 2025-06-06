package com.weatherfit.comment_service.comment.service;

//import com.weatherfit.comment_service.common.mapper.ReplyMapper;
import com.weatherfit.comment_service.comment.controller.CommentController;
import com.weatherfit.comment_service.comment.dto.CommentRequestDTO;
import com.weatherfit.comment_service.comment.dto.CommentResponseDTO;
import com.weatherfit.comment_service.comment.entity.Comment;
import com.weatherfit.comment_service.comment.mapper.CommentMapper;
import com.weatherfit.comment_service.comment.repository.CommentRepository;
import com.weatherfit.comment_service.reply.mapper.ReplyMapper;
import com.weatherfit.comment_service.reply.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentMapper commentMapper;
//    private final ReplyMapper replyMapper;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final ReplyMapper replyMapper;

    @Override
    public Mono<CommentResponseDTO> writeComment(Mono<CommentRequestDTO> dtoMono) {
        return dtoMono
                .flatMap(dto -> {
                    Comment comment = commentMapper.DTOToComment(dto);
                    return commentRepository.save(comment);
                })
                .map(commentMapper::commentToDTO);
    }

    @Override
    public Flux<CommentResponseDTO> getCommentsByBoardId(Long boardId) {
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

//
//    public List<CommentResponseDTO> getCommentsByBoard(int boardId) {
//        List<Comment> comments = commentRepository.findByBoardId(boardId);
//
//        comments.forEach(comment -> comment.getReplyList().size());
//        return commentMapper.commentsToDTOList(comments);
//    }
//
    @Override
    public Mono<Void> deleteComment(Long commentId) {
        return commentRepository.findById(commentId)
                .switchIfEmpty(
                            Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Id에 해당하는 댓글이 없음"))
                )
                .flatMap(comment -> {
                    comment.setStatus(0);
                    return commentRepository.save(comment);
                })
                .then();
    }
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
