package com.Hello.Pet_Shop.services.jwt;

import com.Hello.Pet_Shop.exceptions.CustomValidJwtException;
import com.Hello.Pet_Shop.services.security.UserDetailsServicesImpl;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@NoArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter
{
    @Autowired
    private UserDetailsServicesImpl userDetailsServicesImpl;

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws CustomValidJwtException,ServletException, IOException
    {
        final String authorizationHeader = request.getHeader("Authorization");

        String email = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
        {
            jwt = authorizationHeader.substring(7);
            try {
                email = jwtService.extractEmail(jwt);
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().write("Session is expired! Please log in again!");
                return;           }
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            UserDetails userDetails = this.userDetailsServicesImpl.loadUserByUsername(email);
            if (jwtService.validateToken(jwt,email))
            {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            else throw new CustomValidJwtException("Session is expired! Please log in again!");
        }
        filterChain.doFilter(request, response);
    }
}
