package com.aiykr.iquais.service.impl;

import com.aiykr.iquais.dto.request.UserDto;
import com.aiykr.iquais.dto.request.UserSignInRequest;
import com.aiykr.iquais.dto.response.UserResponse;
import com.aiykr.iquais.dto.response.UserSignInResponse;
import com.aiykr.iquais.entity.User;
import com.aiykr.iquais.repository.UserRepository;
import com.aiykr.iquais.service.UserService;
import com.aiykr.iquais.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    public UserResponse<List<UserDto>> getAllUsers() {
        List<UserDto> userDtoList = new ArrayList<>();
        UserResponse<List<UserDto>> response = new UserResponse<>();

        // Fetch companyData from DB
//        List<User> dataDb = (List<User>)userRepository.findAll();
//
//        if (dataDb.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database is empty");
//        } else {
//            for (int i = 0; i < dataDb.size(); i++) {
//                UserDao user = dataDb.get(i);
//                int id = user.getId();
//                UserDto dto = new UserDto(user);

        // saving in List
//                userDtoList.add(dto);
//            }
//            response.setData(userDtoList);
//            response.setMeta(getAllUsers().getMeta());
//        }
        return response;
    }

    @Override
    public UserSignInResponse userSignIn(UserSignInRequest userSignInRequest) {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userSignInRequest.getUserId(),
                        userSignInRequest.getPassword()
                ));
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userSignInRequest.getUserId());
        String token = jwtUtil.generateToken(userDetails);
        User user=userRepository.findByEmail(userDetails.getUsername());
        UserSignInResponse userSignInResponse=UserSignInResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .grade(null)
                .accessToken(token)
                .build();
        return userSignInResponse;
    }
}
