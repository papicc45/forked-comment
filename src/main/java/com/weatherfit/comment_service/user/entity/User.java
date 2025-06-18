package com.weatherfit.comment_service.user.entity;

import com.weatherfit.comment_service.common.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("Users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    @Id
    private Long id;
    private String userId;
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private Integer status = 1;
    private LocalDateTime passwordChangedAt;
}
