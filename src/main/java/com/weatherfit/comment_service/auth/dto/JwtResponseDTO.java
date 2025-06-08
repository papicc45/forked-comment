package com.weatherfit.comment_service.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class JwtResponseDTO {
    private String token;
    private String tokenType = "Bearer";

    public JwtResponseDTO(String token) {
       this.token = token;
    }
}
