package com.weatherfit.comment_service.reply.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReplyRequestDTO {

    private Integer commentId;

    private String nickname;

    private String content;
}
