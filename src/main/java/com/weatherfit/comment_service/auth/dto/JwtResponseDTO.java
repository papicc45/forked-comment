package com.weatherfit.comment_service.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class JwtResponseDTO {
    private String token;
    private String tokenType = "Bearer";
    private boolean requirePasswordChange;
    public JwtResponseDTO(String token, boolean requirePasswordChange) {
       this.token = token;
       this.requirePasswordChange = requirePasswordChange;
    }
}
