package com.aiykr.iquais.service;

import com.aiykr.iquais.dto.request.UserDto;
import com.aiykr.iquais.dto.request.UserSignInRequest;
import com.aiykr.iquais.dto.response.UserResponse;
import com.aiykr.iquais.dto.response.UserSignInResponse;

import java.util.List;

public interface UserService {
    public UserResponse<List<UserDto>> getAllUsers();

    UserSignInResponse userSignIn(UserSignInRequest userSignInRequest);
}
