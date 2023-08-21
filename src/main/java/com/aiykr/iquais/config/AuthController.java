package com.aiykr.iquais.config;

import com.aiykr.iquais.dto.response.ErrorCodes;
import com.aiykr.iquais.dto.response.Meta;
import com.aiykr.iquais.dto.response.Response;
import com.aiykr.iquais.entity.UserDAO;
import com.aiykr.iquais.exception.IquaisException;
import com.aiykr.iquais.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private IUserRepository userRepository; // Inject your user service

    @PostMapping("/login")
    public ResponseEntity<Response<String>> authenticate(@RequestBody AuthRequest authRequest) throws IquaisException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new IquaisException(HttpStatus.UNAUTHORIZED, ErrorCodes.IQ007, "Invalid credentials");
        }

        final UserDetails userDetails = loadUserByUsername(authRequest.getUsername());
        final String token = jwtConfig.generateToken(userDetails);

        Response<String> response = new Response<>();
        response.setData(token);
        response.setMeta(Meta.builder().statusCode(HttpStatus.OK.value()).message("Authentication successful").build());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Add a method to load user details by username (similar to your JwtUserDetailsService)

    private UserDetails loadUserByUsername(String username) throws IquaisException {
        // Implement your logic to load user details from the database or another source
        // Return a UserDetails object based on the user data
        // You can use your IUserService here
        Optional<UserDAO> userOpt = userRepository.findByEmail(username);
        if(userOpt.isEmpty())
            throw new IquaisException(HttpStatus.NOT_FOUND,ErrorCodes.IQ006,"User Not Found");
        UserDAO userDAO = userOpt.get();
        return new User(userDAO.getEmail(), userDAO.getPassword(), new ArrayList<>());
    }

    // Implement token refresh mechanism if needed

    // Other authentication-related endpoints

}

