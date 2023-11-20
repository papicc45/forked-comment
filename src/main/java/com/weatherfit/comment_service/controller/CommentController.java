package com.weatherfit.comment_service.controller;

import com.weatherfit.comment_service.dto.CommentRepsonseDTO;
import com.weatherfit.comment_service.dto.CommentRequestDTO;
import com.weatherfit.comment_service.dto.ReplyRequestDTO;
import com.weatherfit.comment_service.dto.ReplyResponseDTO;
import com.weatherfit.comment_service.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/test")
    public String test() {
        return "test2";
    }

    @PostMapping("/write")
    public ResponseEntity<CommentRepsonseDTO> writeComment(@RequestBody CommentRequestDTO commentRequestDTO) {
        CommentRepsonseDTO result = commentService.writeComment(commentRequestDTO);
        System.out.println(result.toString());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/reply")
    public ResponseEntity<ReplyResponseDTO> writeReply(@RequestBody ReplyRequestDTO replyRequestDTO) {
        ReplyResponseDTO result = commentService.writeReply(replyRequestDTO);
        System.out.println(result.toString());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/comments")
    public ResponseEntity<List<CommentRepsonseDTO>> getCommentsByBoardId(@RequestParam int boardId) {
        List<CommentRepsonseDTO> result = commentService.getCommentsByBoard(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Boolean> removeComment(@RequestParam int commentId) {
        Boolean result = commentService.removeComment(commentId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/remove/reply")
    public ResponseEntity<Boolean> removeReply(@RequestParam int replyId) {
        Boolean result = commentService.removeReply(replyId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

//    @PatchMapping("/modify")
//    public ResponseEntity<Boolean> modifyComment(@RequestBody CommentRequestDTO commentRequestDTO) {
//        Boolean result = commentService.modifyComment();
//        return ResponseEntity.status(HttpStatus.OK).body(result);
//    }
}
