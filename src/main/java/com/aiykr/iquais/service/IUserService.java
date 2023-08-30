package com.aiykr.iquais.service;

import com.aiykr.iquais.dto.request.PostUserDTO;
import com.aiykr.iquais.dto.response.Response;
import com.aiykr.iquais.dto.response.UserResponseDTO;
import com.aiykr.iquais.exception.IquaisException;

import java.util.List;

/**
 * Service interface for managing user-related operations.
 */
public interface IUserService {

    /**
     * Creates a new user based on the provided information.
     *
     * @param postUserDTO The data required to create the user.
     * @return A response containing user creation details.
     * @throws IquaisException If an error occurs during user creation.
     */
    Response<UserResponseDTO> createUser(PostUserDTO postUserDTO) throws IquaisException;

    /**
     * Retrieves a student's information based on the provided ID.
     *
     * @param id The unique ID of the student to retrieve.
     * @return A response containing the retrieved student's information.
     */
    Response<UserResponseDTO> getStudentById(String id);

    /**
     * Retrieves a list of all users.
     *
     * @return A response containing a list of user information.
     */

    public Response<List<UserResponseDTO>> getAllUsers(int page, int size, String sortBy, String sortOrder) throws IquaisException;
}

