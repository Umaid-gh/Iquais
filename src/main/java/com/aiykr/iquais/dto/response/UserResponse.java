package com.aiykr.iquais.dto.response;

import lombok.Data;

@Data
public class UserResponse<T> {
	private T data;
	Meta meta;
}
