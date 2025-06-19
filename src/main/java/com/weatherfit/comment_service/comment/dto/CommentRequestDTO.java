package com.weatherfit.comment_service.comment.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentRequestDTO {

    private Long boardId;
    private String nickname;
    private String content;
}
