package com.aiykr.iquais.controller;

import com.aiykr.iquais.dto.request.UserDto;
import com.aiykr.iquais.dto.request.UserSignInRequest;
import com.aiykr.iquais.dto.response.UserResponse;
import com.aiykr.iquais.dto.response.UserSignInResponse;
import com.aiykr.iquais.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class UserController {
	
	@Autowired
	UserService userService;

	@GetMapping("/users")
	public UserResponse<List<UserDto>> getAllUsers() {
		return userService.getAllUsers();
	}

	@PostMapping("/signin")
	public ResponseEntity<UserSignInResponse> userSignIn(@RequestBody UserSignInRequest userSignInRequest){
		UserSignInResponse userSignInResponse=userService.userSignIn(userSignInRequest);
		return new ResponseEntity<>(userSignInResponse, HttpStatus.OK);
	}
}
