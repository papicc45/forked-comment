package com.weatherfit.comment_service.reply.controller;

import com.weatherfit.comment_service.reply.dto.ReplyRequestDTO;
import com.weatherfit.comment_service.reply.dto.ReplyResponseDTO;
import com.weatherfit.comment_service.reply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ReplyResponseDTO> create(@RequestBody Mono<ReplyRequestDTO> dtoMono) {
        return dtoMono.flatMap(replyService::writeReply);
    }
//    @PostMapping("/reply")
//    public ResponseEntity<ReplyResponseDTO> writeReply(@RequestBody ReplyRequestDTO replyRequestDTO){
//        ReplyResponseDTO result = commentService.writeReply(replyRequestDTO);
//        return ResponseEntity.status(HttpStatus.OK).body(result);
//    }
}
