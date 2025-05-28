package com.weatherfit.comment_service.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserResponseDTO {
    private Long id;
    private String userId;
    private String nickname;
    private String createdDate;
    private String modifiedDate;

}
