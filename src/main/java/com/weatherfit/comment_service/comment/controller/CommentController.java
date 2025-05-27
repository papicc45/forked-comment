package com.weatherfit.comment_service.comment.controller;

import com.weatherfit.comment_service.comment.dto.CommentRequestDTO;
import com.weatherfit.comment_service.comment.dto.CommentResponseDTO;
import com.weatherfit.comment_service.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CommentResponseDTO> create(@RequestBody Mono<CommentRequestDTO> dtoMono) {
        return dtoMono.flatMap(commentService::writeComment);
    }
//    @PostMapping
//    public ResponseEntity<CommentResponseDTO> writeComment(@RequestBody CommentRequestDTO commentRequestDTO) {
//        CommentResponseDTO result = commentService.writeComment(commentRequestDTO);
//        return ResponseEntity.status(HttpStatus.OK).body(result);
//    }

    @GetMapping
    public Flux<CommentResponseDTO> getCommentsByBoardId(@RequestParam Integer boardId) {
        return commentService.getCommentsByBoardId(boardId);
    }
//
//    @GetMapping("/comments")
//    public ResponseEntity<List<CommentResponseDTO>> getCommentsByBoardId(@RequestParam int boardId) {
//        List<CommentResponseDTO> result = commentService.getCommentsByBoard(boardId);
//        return ResponseEntity.status(HttpStatus.OK).body(result);
//    }
//
//    @DeleteMapping("/remove")
//    public ResponseEntity<Boolean> removeComment(@RequestParam int commentId) {
//        Boolean result = commentService.removeComment(commentId);
//        return ResponseEntity.status(HttpStatus.OK).body(result);
//    }
//
//    @DeleteMapping("/remove/reply")
//    public ResponseEntity<Boolean> removeReply(@RequestParam int replyId) {
//        Boolean result = commentService.removeReply(replyId);
//        return ResponseEntity.status(HttpStatus.OK).body(result);
//    }
//
//    @PatchMapping("/modify")
//    public ResponseEntity<Boolean> modifyComment(@RequestBody CommentRequestDTO commentRequestDTO) {
//        Boolean result = commentService.modifyComment(commentRequestDTO);
//        return ResponseEntity.status(HttpStatus.OK).body(result);
//    }
//
//    @PatchMapping("/modify/reply")
//    public ResponseEntity<Boolean> modifyReply(@RequestBody ReplyRequestDTO replyRequestDTO) {
//        Boolean result = commentService.modifyReply(replyRequestDTO);
//        return ResponseEntity.status(HttpStatus.OK).body(result);
//    }
}
