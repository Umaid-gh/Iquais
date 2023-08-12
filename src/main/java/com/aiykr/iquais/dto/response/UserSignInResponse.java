package com.aiykr.iquais.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignInResponse {
    public String name;

    public String email;

    public String phoneNumber;

    public String grade;

    public String accessToken;
}
