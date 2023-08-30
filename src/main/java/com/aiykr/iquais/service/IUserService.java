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
     * Retrieves a user's information based on the provided ID.
     *
     * @param id The unique ID of the user to retrieve.
     * @return A response containing the retrieved user's information.
     */
    Response<UserResponseDTO> getStudentById(String id);

    /**
     * Retrieves a list of all users by page.
     *
     * @param page      The page number to retrieve.
     * @param size      The number of records per page.
     * @param sortBy    The field to sort by.
     * @param sortOrder The sorting order (asc or desc).
     * @return A response containing a list of user information.
     * @throws IquaisException If an error occurs while retrieving the users.
     */
    Response<List<UserResponseDTO>> getAllUsers(int page, int size, String sortBy, String sortOrder) throws IquaisException;

    /**
     * Soft Deletes a user by email address.
     *
     * @param email The email address of the user to delete.
     * @return A response containing user information.
     * @throws IquaisException If an error occurs during the deletion process.
     */
    Response<UserResponseDTO> deleteStudentByEmail(String email) throws IquaisException;
}
