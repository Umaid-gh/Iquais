package com.aiykr.iquais.controller;

import com.aiykr.iquais.dto.request.PostUserDTO;
import com.aiykr.iquais.dto.response.Response;
import com.aiykr.iquais.dto.response.UserResponseDTO;
import com.aiykr.iquais.exception.ResponseExceptionHandler;
import com.aiykr.iquais.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class UserController {
	
	@Autowired
	UserService userService;

	@GetMapping("/users/all")
	public ResponseEntity<Response<List<UserResponseDTO>>> getAllUsers() {
		return userService.getAllUsers();
	}
	@PostMapping("/users/student")
	public ResponseEntity<Response> createStudent(@RequestBody PostUserDTO postUserDTO) throws ResponseExceptionHandler {
		return userService.createUser(postUserDTO);
	}

	@GetMapping("/users")
	public ResponseEntity<Response<UserResponseDTO>> getStudentById(@PathParam("id") String id) {
		return userService.getStudentById(id);
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

