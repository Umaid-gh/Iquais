package com.aiykr.iquais.dto.response;

import lombok.Data;

@Data
public class Response<T> {
	private T data;
	Meta meta;
}
