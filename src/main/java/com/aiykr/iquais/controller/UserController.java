package com.aiykr.iquais.controller;

import com.aiykr.iquais.dto.request.UserDto;
import com.aiykr.iquais.dto.request.UserSignInRequest;
import com.aiykr.iquais.dto.request.UserSignupReq;
import com.aiykr.iquais.dto.response.UserResponse;
import com.aiykr.iquais.dto.response.UserSignInResponse;
import com.aiykr.iquais.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class UserController {
	
	@Autowired
	UserService userService;

	@GetMapping("/users")
	public UserResponse<List<UserDto>> getAllUsers() {
		System.out.println("Hello....");
		System.out.println("Able to access get all users through token");
		return userService.getAllUsers();

	}

	@PostMapping("/signin")
	public ResponseEntity<UserSignInResponse> userSignIn(@RequestBody UserSignInRequest userSignInRequest){
		System.out.println("HIIIIIIIIIII.....");
		System.out.println(userSignInRequest);
		try {
			UserSignInResponse userSignInResponse = userService.userSignIn(userSignInRequest);
			return new ResponseEntity<>(userSignInResponse, HttpStatus.OK);
		}
		catch (UsernameNotFoundException e){
			throw  new UsernameNotFoundException("Username or passoword is invalid");
		}
		catch (Exception e){
			throw e;
		}
	}

	@PostMapping("/signup")
	public ResponseEntity<Void> signup(@RequestBody UserSignupReq userSignupReq)
	{
		 userService.signup(userSignupReq);
		 return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
