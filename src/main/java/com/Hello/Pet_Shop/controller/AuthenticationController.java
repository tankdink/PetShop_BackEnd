package com.Hello.Pet_Shop.controller;

import com.Hello.Pet_Shop.auth.AuthenticationRequest;
import com.Hello.Pet_Shop.auth.AuthenticationResponse;
import com.Hello.Pet_Shop.entity.User;
import com.Hello.Pet_Shop.exceptions.ResourceNotFoundException;
import com.Hello.Pet_Shop.mapper.UserMapper;
import com.Hello.Pet_Shop.services.jwt.JwtService;
import com.Hello.Pet_Shop.services.security.UserDetailsServicesImpl;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@RestController
@RequestMapping("/authen")
@AllArgsConstructor
@RequiredArgsConstructor
public class AuthenticationController
{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServicesImpl userDetailsServicesImpl;

    @Autowired
    private JwtService jwtService;

    //Authenticate
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticaionToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception
    {
        try
        {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),authenticationRequest.getPassword()));
        } catch (BadCredentialsException e)
        {
            e.printStackTrace();
            throw new Exception("Invalid email or password!",e);
        }

        //Custom login here

        final UserDetails userDetails = userDetailsServicesImpl.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtService.generateToken(userDetails.getUsername());
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    //Get user logged-in information
    @GetMapping("/loggedIn-data")
    public ResponseEntity<?> getLoggedInUserData() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) {
            throw new ResourceNotFoundException("User not found");
        }
        User gettedLoggedInUserDto = userDetailsServicesImpl.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(UserMapper.mapToUserDto(gettedLoggedInUserDto));
    }
}
