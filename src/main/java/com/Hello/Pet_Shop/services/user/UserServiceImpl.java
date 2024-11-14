package com.Hello.Pet_Shop.services.user;

import com.Hello.Pet_Shop.dto.UserDto;
import com.Hello.Pet_Shop.entity.Role;
import com.Hello.Pet_Shop.entity.User;
import com.Hello.Pet_Shop.exceptions.BadInputException;
import com.Hello.Pet_Shop.exceptions.DuplicateEntryException;
import com.Hello.Pet_Shop.exceptions.PermissionException;
import com.Hello.Pet_Shop.exceptions.ResourceNotFoundException;
import com.Hello.Pet_Shop.mapper.UserMapper;
import com.Hello.Pet_Shop.repository.RoleRepository;
import com.Hello.Pet_Shop.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Data
class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createNewUser(UserDto userDto)
    {
        UserDto newUser = new UserDto(userDto.getId(), userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()),true, userDto.getRole());
        User savedUser = userRepository.save(UserMapper.mapToUser(newUser));
        return UserMapper.mapToUserDto(savedUser);
    }


    @Override
    public UserDto getUserByID(Long userID)
    {
        User gettedUser = userRepository.findById(userID)
                .orElseThrow( ()->new ResourceNotFoundException("User is not exists with given ID: " +userID) );
        return UserMapper.mapToUserDto(gettedUser);
    }

    @Override
    public List<UserDto> getUserByFirstName(String firstName)
    {
        List<User> gettedUserList = userRepository.findByfirstName(firstName);
        if (gettedUserList.isEmpty()) throw new ResourceNotFoundException("User not found!");
        else return gettedUserList.stream()
                .map( (User) -> UserMapper.mapToUserDto(User) )
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User gettedUser = userRepository.findByEmail(email);
        if (gettedUser == null) throw new ResourceNotFoundException("User is not exists with given email: " +email);
        return UserMapper.mapToUserDto(gettedUser);
    }

    @Override
    public Role getRolesByID(Long userID) {
        User gettedUser = userRepository.findById(userID)
                .orElseThrow( ()->new ResourceNotFoundException("User is not exists with given ID: " +userID) );
        return gettedUser.getRole();
    }

    @Override
    public List<UserDto> getAllUser() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map( (User) -> UserMapper.mapToUserDto(User) )
                .collect(Collectors.toList());
    }


    @Override
    public Boolean checkPasswordInput(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&         // At least one uppercase letter
                password.matches(".*[a-z].*") &&         // At least one lowercase letter
                password.matches(".*\\d.*") &&           // At least one digit
                password.matches(".*[!@#$%^&*(),.?\":{}|<>].*") && // At least one special character
                !password.matches(".*\\s.*");
    }

    @Override
    public UserDto updatePassword(Long userID, String password) {
        User gettedUser = userRepository.findById(userID)
                .orElseThrow( ()-> new ResourceNotFoundException("User is not exists with given ID: " +userID) );

        //Check password input
        if (!checkPasswordInput(password))  throw new BadInputException("Password must contain at least 7 characters, one uppercase letter, one lowercase letter, one number, one special character (!@*/...) and no whitespace!");


        //Valid the user role, ID of logged-in user and ID of the user's being updated
        UserDetails userDetails = null;
        try {
            userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (ClassCastException e) {
            throw new ResourceNotFoundException("Please logged in to use our services!");
        }

        User loggedInUser = userRepository.findByEmail(userDetails.getUsername());
        Integer idNow = loggedInUser.getId();
        if (!userID.equals(idNow.longValue())) throw new PermissionException("You don't have enough permission to this!");




        gettedUser.setPassword(passwordEncoder.encode(password));

        User updatedUserObj = null;

        try {
            updatedUserObj = userRepository.save(gettedUser);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return UserMapper.mapToUserDto(updatedUserObj);
    }

    @Override
    public UserDto updateUser(Long userID, UserDto updatedUser)
    {
        User gettedUser = userRepository.findById(userID)
                .orElseThrow( ()-> new ResourceNotFoundException("User is not exists with given ID: " +userID) );


        //Valid the user role, ID of logged-in user and ID of the user's being updated
        UserDetails userDetails = null;
        try {
            userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (ClassCastException e) {
            throw new ResourceNotFoundException("Please logged in to use our services!");
        }

        UserDto loggedInUser = getUserByEmail(userDetails.getUsername());
        String roleNow = loggedInUser.getRole().getName();
        Integer idNow = loggedInUser.getId();
        String emailNow = loggedInUser.getEmail();
        if (Objects.equals(roleNow, "ROLE_ACCOUNT_MANAGER") || Objects.equals(roleNow, "ROLE_USER"))
        {
            if (!userID.equals(idNow.longValue())) throw new PermissionException("You don't have enough permission to this!");
        }

        //Check duplicated email (allow user to use their email again due to the use API in front-end)
        if (userRepository.existsByEmail(updatedUser.getEmail()) && !Objects.equals(emailNow, updatedUser.getEmail())) throw new DuplicateEntryException("Email has been used!");

        gettedUser.setFirstName(updatedUser.getFirstName());
        gettedUser.setLastName(updatedUser.getLastName());
        gettedUser.setEmail(updatedUser.getEmail());

        User updatedUserObj = null;
        try {
            updatedUserObj = userRepository.save(gettedUser);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
        }

        return UserMapper.mapToUserDto(updatedUserObj);
    }

    @Override
    public void deleteUserByID(Long userID)
    {
        User gettedUser = userRepository.findById(userID)
                .orElseThrow( ()-> new ResourceNotFoundException("User is not exists with given ID: " +userID) );
        userRepository.deleteById(userID);
    }

    @Override
    public void deleteAllUser() {
        userRepository.deleteAll();
    }
}
