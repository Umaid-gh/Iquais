package com.aiykr.iquais.controller;

import com.aiykr.iquais.dto.request.PostUserDTO;
import com.aiykr.iquais.dto.response.Response;
import com.aiykr.iquais.dto.response.UserResponseDTO;
import com.aiykr.iquais.exception.IquaisException;
import com.aiykr.iquais.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	@Autowired
	UserService userService;

	@GetMapping("/users/all")
	public ResponseEntity<Response<List<UserResponseDTO>>> getAllUsers() {
		return userService.getAllUsers();
	}
	@PostMapping("/users/student")
	public ResponseEntity<Response> createStudent(@RequestBody PostUserDTO postUserDTO) throws IquaisException {
		return userService.createUser(postUserDTO);
	}

	@GetMapping("/users")
	public ResponseEntity<Response<UserResponseDTO>> getStudentById(@PathParam("id") String id) {
		return userService.getStudentById(id);
	}
}
