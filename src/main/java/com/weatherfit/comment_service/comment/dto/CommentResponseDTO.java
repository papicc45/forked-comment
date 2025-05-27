package com.weatherfit.comment_service.comment.dto;

import com.weatherfit.comment_service.reply.dto.ReplyResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CommentResponseDTO {
    private Integer id;
    private Integer boardId;
    private String nickname;
    private String content;

    private String createdDate;
    private String createdTime;

    private int status;

    private List<ReplyResponseDTO> replyList;
}