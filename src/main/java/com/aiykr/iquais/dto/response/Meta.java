package com.aiykr.iquais.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Meta {
    private String message;
    private int status;
}
