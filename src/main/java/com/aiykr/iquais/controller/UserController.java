package com.aiykr.iquais.controller;

import com.aiykr.iquais.dto.request.PostUserDTO;
import com.aiykr.iquais.dto.response.Response;
import com.aiykr.iquais.dto.response.UserResponseDTO;
import com.aiykr.iquais.exception.IquaisException;
import com.aiykr.iquais.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing user-related API endpoints.
 */
@RestController
@RequestMapping("/v1")
@Api(tags = "User API")
@Builder
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    IUserService userService;

    /**
     * Creates a new student user.
     *
     * @param postUserDTO The data required to create the student.
     * @return A ResponseEntity containing the response with the created student details.
     * @throws IquaisException If an error occurs during student creation.
     */
    @PostMapping("/user")
    @ApiOperation("Create a new student user")
    public ResponseEntity<Response<UserResponseDTO>> createStudent(@RequestBody PostUserDTO postUserDTO) throws IquaisException {
        log.info("Endpoint: Create New Student API");
        Response<UserResponseDTO> response = userService.createUser(postUserDTO);
        return ResponseEntity.status(response.getMeta().getStatusCode()).body(response);
    }

    /**
     * Retrieves a user's information based on the provided ID.
     *
     * @param id The unique ID of the user to retrieve.
     * @return A ResponseEntity containing the response with the retrieved user details.
     */
    @GetMapping("/user/{id}")
    @ApiOperation("Get a user by ID")
    public ResponseEntity<Response<UserResponseDTO>> getStudentById(@PathVariable String id) {
        log.info("Endpoint: Get Student by ID API");
        Response<UserResponseDTO> response = userService.getStudentById(id);
        return ResponseEntity.status(response.getMeta().getStatusCode()).body(response);
    }

    /**
     * Retrieves a list of all users by page.
     *
     * @param page      The page number to retrieve.
     * @param size      The number of records per page.
     * @param sortBy    The field to sort by.
     * @param sortOrder The sorting order (asc or desc).
     * @return A ResponseEntity containing the response with the list of users.
     * @throws IquaisException If an error occurs while retrieving the users.
     */
    @GetMapping("/users")
    public ResponseEntity<Response<List<UserResponseDTO>>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) throws IquaisException {

        log.info("Endpoint: Get Student data by Page");
        Response<List<UserResponseDTO>> response = userService.getAllUsers(page, size, sortBy, sortOrder);
        return ResponseEntity.status(response.getMeta().getStatusCode()).body(response);
    }

    /**
     * Deletes a student user by email.
     *
     * @param email The email address of the student to delete.
     * @return A ResponseEntity containing the response with the result of the deletion.
     * @throws IquaisException If an error occurs during the deletion process.
     */
    @DeleteMapping("/user/{email}")
    public ResponseEntity<Response<UserResponseDTO>> deleteStudentByEmail(@PathVariable String email) throws IquaisException {
        log.info("Endpoint: Delete Student by Email");
        Response<UserResponseDTO> response = userService.deleteStudentByEmail(email);
        return ResponseEntity.status(response.getMeta().getStatusCode()).body(response);
    }
}
