package com.weatherfit.comment_service.reply.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ReplyResponseDTO {

    private Integer id;

    private String nickname;

    private String content;

    private Integer status;

    private String createdDate;

    private String createdTime;
}
