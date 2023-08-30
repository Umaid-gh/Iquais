package com.aiykr.iquais.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data class representing the API response structure containing data and metadata.
 *
 * @param <T> The type of data contained in the response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Response<T> {

	/**
	 * The data payload of the response.
	 */
	private T data;

	/**
	 * The metadata information providing details about the response.
	 */
	private Meta meta;
}
