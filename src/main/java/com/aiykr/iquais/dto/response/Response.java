package com.aiykr.iquais.dto.response;

import lombok.Data;

/**
 * Data class representing the API response structure containing data and metadata.
 *
 * @param <T> The type of data contained in the response.
 */
@Data
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
