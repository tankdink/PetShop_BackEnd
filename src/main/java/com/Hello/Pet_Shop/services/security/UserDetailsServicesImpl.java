package com.Hello.Pet_Shop.services.security;

import com.Hello.Pet_Shop.entity.User;
import com.Hello.Pet_Shop.exceptions.CustomBadCredentialsException;
import com.Hello.Pet_Shop.exceptions.ResourceNotFoundException;
import com.Hello.Pet_Shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServicesImpl implements UserDetailsService
{
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws CustomBadCredentialsException, ResourceNotFoundException
    {
        User gettedUser = userRepository.findByEmail(email);

        if (gettedUser == null) throw new ResourceNotFoundException("Please logged in to use our services!");

        //Account isEnable?
        boolean isEnable = gettedUser.isEnabled();
        if (!isEnable) {
            throw new CustomBadCredentialsException("Your account has been deactivated! Please contatct us for more information!");
        }

        return new org.springframework.security.core.userdetails
                .User(gettedUser.getEmail(),
                      gettedUser.getPassword(),
                      isEnable,
                     true,
                     true,
                     true,
                      Collections.singletonList(new SimpleGrantedAuthority(gettedUser.getRole().getName())));
    }

    public User getUserByEmail(String email)
    {
        return userRepository.findByEmail(email);
    }
}
