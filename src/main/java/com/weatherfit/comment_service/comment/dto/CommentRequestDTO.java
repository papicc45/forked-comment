package com.weatherfit.comment_service.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CommentRequestDTO {

    private Integer boardId;
    private String nickname;
    private String content;
}
