package com.aiykr.iquais.service;

import com.aiykr.iquais.dto.request.PostUserDTO;
import com.aiykr.iquais.dto.response.Response;
import com.aiykr.iquais.dto.response.UserResponseDTO;
import com.aiykr.iquais.exception.IquaisException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    public ResponseEntity<Response> createUser(PostUserDTO postUserDTO) throws IquaisException;

    ResponseEntity<Response<UserResponseDTO>> getStudentById(String id);

    public ResponseEntity<Response<List<UserResponseDTO>>> getAllUsers();
}
