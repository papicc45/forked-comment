package com.weatherfit.comment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CommentRepsonseDTO {
    private int id;
    private int boardId;
    private String nickname;
    private String content;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private List<ReplyResponseDTO> replyList;
}
