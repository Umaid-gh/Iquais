package com.aiykr.iquais.controller;

import com.aiykr.iquais.dto.request.PostUserDTO;
import com.aiykr.iquais.dto.response.Response;
import com.aiykr.iquais.dto.response.UserResponseDTO;
import com.aiykr.iquais.exception.IquaisException;
import com.aiykr.iquais.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing user-related API endpoints.
 */
@RestController
@RequestMapping("/v1")
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
    @PostMapping("/users/student")
    public ResponseEntity<Response<UserResponseDTO>> createStudent(@RequestBody PostUserDTO postUserDTO) throws IquaisException {
        log.info("Create New Student API");
        Response<UserResponseDTO> response = userService.createUser(postUserDTO);
        return ResponseEntity.status(response.getMeta().getStatusCode()).body(response);
    }

    /**
     * Retrieves a user's information based on the provided ID.
     *
     * @param id The unique ID of the user to retrieve.
     * @return A ResponseEntity containing the response with the retrieved user details.
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<Response<UserResponseDTO>> getStudentById(@PathVariable String id) {
        log.info("Get Student by ID API");
        Response<UserResponseDTO> response = userService.getStudentById(id);
        return ResponseEntity.status(response.getMeta().getStatusCode()).body(response);
    }

    /**
     * Retrieves a list of all users by page.
     *
     * @param page   The number of page to retrieve.
     * @param size   Size of the page needed to retrieve.
     * @param sortBy The unique ID of the users.
     * @return A ResponseEntity containing the response with the list of users.
     */
    @GetMapping("/users")
    public ResponseEntity<Response<List<UserResponseDTO>>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {

        log.info("Get Student data by Page");
        Response<List<UserResponseDTO>> response = userService.getAllUsers(page, size, sortBy, sortOrder);
        return ResponseEntity.status(response.getMeta().getStatusCode()).body(response);
    }
}
