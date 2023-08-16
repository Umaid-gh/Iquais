package com.aiykr.iquais.service;

import com.aiykr.iquais.dto.request.PostUserDTO;
import com.aiykr.iquais.dto.response.Response;
import com.aiykr.iquais.dto.response.UserResponseDTO;
import com.aiykr.iquais.exception.IquaisException;

import java.util.List;

public interface UserService {
    Response<UserResponseDTO> createUser(PostUserDTO postUserDTO) throws IquaisException;

    Response<UserResponseDTO> getStudentById(String id);

    Response<List<UserResponseDTO>> getAllUsers();
}
