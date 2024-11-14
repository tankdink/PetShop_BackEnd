package com.Hello.Pet_Shop.controller;

import com.Hello.Pet_Shop.auth.AuthenticationRequest;
import com.Hello.Pet_Shop.auth.AuthenticationResponse;
import com.Hello.Pet_Shop.entity.User;
import com.Hello.Pet_Shop.exceptions.CustomBadCredentialsException;
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
    public ResponseEntity<?> createAuthenticaionToken(@RequestBody AuthenticationRequest authenticationRequest) throws CustomBadCredentialsException
    {

        User user = userDetailsServicesImpl.getUserByEmail(authenticationRequest.getEmail());
        if (user == null) {
            throw new CustomBadCredentialsException("Wrong email or password!");
        }
        try
        {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),authenticationRequest.getPassword()));
        } catch (BadCredentialsException e)
        {
            e.printStackTrace();
            throw new CustomBadCredentialsException("Wrong email or password!");
        }

        //Custom login here

        final UserDetails userDetails = userDetailsServicesImpl.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtService.generateToken(userDetails.getUsername());
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    //Get user logged-in information
    @GetMapping("/loggedIn-data")
    public ResponseEntity<?> getLoggedInUserData() {

        UserDetails userDetails = null;
        try {
            userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (ClassCastException e) {
            throw new ResourceNotFoundException("Please logged in to use our services!");
        }

        if (userDetails == null) {
            throw new ResourceNotFoundException("Failed when fetching data with this user");
        }
        User gettedLoggedInUserDto = userDetailsServicesImpl.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(UserMapper.mapToUserDto(gettedLoggedInUserDto));
    }
}
