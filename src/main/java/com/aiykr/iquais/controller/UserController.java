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

import javax.websocket.server.PathParam;
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
     * Retrieves a list of all users.
     *
     * @return A ResponseEntity containing the response with the list of users.
     */
    @GetMapping("/users/all")
    public ResponseEntity<Response<List<UserResponseDTO>>> getAllUsers() {
        log.info("Fetch All Users API");
        Response<List<UserResponseDTO>> response = userService.getAllUsers();
        return ResponseEntity.status(response.getMeta().getStatusCode()).body(response);
    }

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

	@GetMapping("/users/getByPage")
	public ResponseEntity<Response<List<UserResponseDTO>>> getUsersByPagination(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String sortOrder){

		Response<List<UserResponseDTO>> response = userService.getAllUsersByPagination(page, size, sortBy, sortOrder);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
