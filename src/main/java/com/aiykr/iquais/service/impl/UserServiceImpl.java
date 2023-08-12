package com.aiykr.iquais.service.impl;

import com.aiykr.iquais.dto.request.UserDao;
import com.aiykr.iquais.dto.request.UserDto;
import com.aiykr.iquais.dto.request.UserSignInRequest;
import com.aiykr.iquais.dto.request.UserSignupReq;
import com.aiykr.iquais.dto.response.UserResponse;
import com.aiykr.iquais.dto.response.UserSignInResponse;
import com.aiykr.iquais.entity.User;
import com.aiykr.iquais.exception.InvalidCredentials;
import com.aiykr.iquais.repository.UserRepository;
import com.aiykr.iquais.service.UserService;
import com.aiykr.iquais.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userSignInRequest.getUserId(),
                    userSignInRequest.getPassword()
            ));
        }
        catch (BadCredentialsException e){
            throw  new InvalidCredentials("Either Username or Password is incorrect");
        }
          UserDetails userDetails = customUserDetailsService.loadUserByUsername(userSignInRequest.getUserId());
          System.out.println("userDetails = " + userDetails);
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

    @Override
    public void signup(UserSignupReq userSignupReq) {


        User user=new User();
        user.setEmail(userSignupReq.getEmail());
        user.setPassword(passwordEncoder.encode(userSignupReq.getPassword()));
        user.setName(userSignupReq.getName());
        user.setPhoneNumber("12343");
        user.setType("STUDENT");
        User user1=userRepository.save(user);
        System.out.println("user1 = " + user1);
//        User user=User.builder()
//                .email(userSignupReq.getEmail())
//                .password(passwordEncoder.encode(userSignupReq.getPassword()))
//                .name(userSignupReq.getName())
//                .phoneNumber("1111")
//                .type("STUDENT")
//                .build();
//
//           User user1= userRepository.save(user);
//        System.out.println(user1);



    }
}
