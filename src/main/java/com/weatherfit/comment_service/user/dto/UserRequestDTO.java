package com.weatherfit.comment_service.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserRequestDTO {

    @NotBlank
    private String userId;

    @NotBlank
    @Size(min = 8, max = 16)
    private String password;

    @NotBlank
    private String nickname;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phone;
}
