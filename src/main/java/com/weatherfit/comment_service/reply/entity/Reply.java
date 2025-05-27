package com.weatherfit.comment_service.reply.entity;

import com.weatherfit.comment_service.common.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table("reply")
public class Reply extends BaseEntity {

    @Id
    private Integer id;

    private String nickname;

    private String content;

    private Integer status = 1;

    private Integer commentId;

}