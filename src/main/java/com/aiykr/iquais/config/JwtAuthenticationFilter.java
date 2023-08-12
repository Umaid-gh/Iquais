package com.aiykr.iquais.config;

import com.aiykr.iquais.service.impl.CustomUserDetailsService;
import com.aiykr.iquais.util.JwtUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestHeader=request.getHeader("Authorization");
        String username=null;
        String jwtToken=null;
        if(requestHeader!=null&&requestHeader.startsWith("Bearer ")){
            jwtToken=requestHeader.substring(7);
            try {
                username=jwtUtil.extractUsername(jwtToken);
            }
           catch (UsernameNotFoundException e){
                e.printStackTrace();
           }
            catch (Exception e){
                e.printStackTrace();
            }
            UserDetails userDetails=customUserDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                    new UsernamePasswordAuthenticationToken(userDetails,null,null);
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        else{
            System.out.println("This token is invalid");
        }
        filterChain.doFilter(request,response);
    }
}
