package com.aiykr.iquais.service;

import com.aiykr.iquais.dto.request.UserDto;
import com.aiykr.iquais.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    public UserResponse<List<UserDto>> getAllUsers();
}
