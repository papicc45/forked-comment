package com.weatherfit.comment_service.comment.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.weatherfit.comment_service.reply.dto.ReplyResponseDTO;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
public class CommentResponseDTO {
    private Long id;
    private Long boardId;
    private String nickname;
    private String content;

    private String createdDate;
    private String modifiedDate;

    private int status;

    private List<ReplyResponseDTO> replyList;
}