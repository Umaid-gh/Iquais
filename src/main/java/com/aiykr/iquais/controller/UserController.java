package com.aiykr.iquais.controller;

import com.aiykr.iquais.dto.request.UserDto;
import com.aiykr.iquais.dto.response.UserResponse;
import com.aiykr.iquais.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
