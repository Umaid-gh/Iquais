package com.aiykr.iquais.config;

import com.aiykr.iquais.dto.response.ErrorCodes;
import com.aiykr.iquais.entity.UserDAO;
import com.aiykr.iquais.exception.IquaisException;
import com.aiykr.iquais.repository.IUserRepository;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(JwtUserDetailsService.class);

    @Autowired
    private IUserRepository userRepository;

    @SneakyThrows
    public UserDetails loadUserByUsername(String email) {
        Optional<UserDAO> optionalUserOpt;
        UserDAO userDAO ;
        try {

            optionalUserOpt = userRepository.findByEmail(email);

            if (optionalUserOpt.isEmpty())
                throw new IquaisException(HttpStatus.NOT_FOUND, ErrorCodes.IQ006, "User Email not present in our DB");
        } catch (IquaisException iqEx) {
            throw iqEx;
        }
        userDAO = optionalUserOpt.get();
        log.info("User Found : {}", userDAO.getEmail());
        return new User(userDAO.getEmail(), userDAO.getPassword(), new ArrayList<>());
    }
}

