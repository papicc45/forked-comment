package com.weatherfit.comment_service.controller;

import com.weatherfit.comment_service.common.JwtTokenProvider;
import com.weatherfit.comment_service.dto.CommentRepsonseDTO;
import com.weatherfit.comment_service.dto.CommentRequestDTO;
import com.weatherfit.comment_service.dto.ReplyRequestDTO;
import com.weatherfit.comment_service.dto.ReplyResponseDTO;
import com.weatherfit.comment_service.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final JwtTokenProvider jwtTokenProvider;
    private final CommentService commentService;
    @GetMapping("/jwtTest")
    public String jwtTest(@RequestHeader("decodedToken") String decodedToken) {
        String result = decodedToken + " -> jwt success";
        return result;
    }

    @GetMapping("/jwtTest2")
    public String jwtTest2() {
        String token = jwtTokenProvider.generateToken("test2");

        return token;
    }

    @PostMapping("/write")
    public ResponseEntity<CommentRepsonseDTO> writeComment(@RequestBody CommentRequestDTO commentRequestDTO) {
        CommentRepsonseDTO result = commentService.writeComment(commentRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/reply")
    public ResponseEntity<ReplyResponseDTO> writeReply(@RequestBody ReplyRequestDTO replyRequestDTO) {
        ReplyResponseDTO result = commentService.writeReply(replyRequestDTO);
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

    @PatchMapping("/modify")
    public ResponseEntity<Boolean> modifyComment(@RequestBody CommentRequestDTO commentRequestDTO) {
        Boolean result = commentService.modifyComment(commentRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PatchMapping("/modify/reply")
    public ResponseEntity<Boolean> modifyReply(@RequestBody ReplyRequestDTO replyRequestDTO) {
        Boolean result = commentService.modifyReply(replyRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
