package com.Hello.Pet_Shop.services.security;

import com.Hello.Pet_Shop.entity.User;
import com.Hello.Pet_Shop.exceptions.ResourceNotFoundException;
import com.Hello.Pet_Shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServicesImpl implements UserDetailsService
{
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
    {
        User gettedUser = userRepository.findByEmail(email);
        if (gettedUser == null) throw new ResourceNotFoundException("User is not exists with given email: " +email);
        boolean isEnable = gettedUser.isEnabled();
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
