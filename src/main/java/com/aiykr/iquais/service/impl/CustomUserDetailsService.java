package com.aiykr.iquais.service.impl;

import com.aiykr.iquais.entity.CustomUserDetails;
import com.aiykr.iquais.entity.User;
import com.aiykr.iquais.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if(user == null){
            throw  new UsernameNotFoundException("User with username "+ username+" not found");
        }
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        return customUserDetails;
    }
}
