package com.aiykr.iquais.dto.response;

import lombok.Builder;
import lombok.Data;

/**
 * Data class representing metadata information for API responses.
 */
@Data
@Builder
public class Meta {

    /**
     * The message providing additional information about the response.
     */
    private String message;

    /**
     * The error code indicating the specific error scenario.
     */
    private String errorCode;

    /**
     * The HTTP status code of the response.
     */
    private int statusCode;
}
