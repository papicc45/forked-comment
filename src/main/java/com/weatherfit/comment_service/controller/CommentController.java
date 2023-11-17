package com.weatherfit.comment_service.controller;

import com.weatherfit.comment_service.dto.CommentRepsonseDTO;
import com.weatherfit.comment_service.dto.CommentRequestDTO;
import com.weatherfit.comment_service.dto.ReplyRequestDTO;
import com.weatherfit.comment_service.dto.ReplyResponseDTO;
import com.weatherfit.comment_service.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
