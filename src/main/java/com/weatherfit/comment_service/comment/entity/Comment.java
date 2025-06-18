package com.weatherfit.comment_service.comment.entity;

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
@Table("comment")
public class Comment extends BaseEntity {

    @Id
    private Long id;

    private Integer boardId;

    private String nickname;

    private String content;

    private Integer status = 1;
}
