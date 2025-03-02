package com.Hello.Pet_Shop.services.user;

import com.Hello.Pet_Shop.dto.RoleDto;
import com.Hello.Pet_Shop.dto.UserDto;
import com.Hello.Pet_Shop.entity.Role;
import com.Hello.Pet_Shop.entity.User;
import com.Hello.Pet_Shop.exceptions.BadInputException;
import com.Hello.Pet_Shop.exceptions.DuplicateEntryException;
import com.Hello.Pet_Shop.exceptions.PermissionException;
import com.Hello.Pet_Shop.exceptions.ResourceNotFoundException;
import com.Hello.Pet_Shop.mapper.RoleMapper;
import com.Hello.Pet_Shop.mapper.UserMapper;
import com.Hello.Pet_Shop.repository.RoleRepository;
import com.Hello.Pet_Shop.repository.UserRepository;
import com.Hello.Pet_Shop.services.security.UserDetailsServicesImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private UserDetailsServicesImpl userDetailsServicesImpl;

    @Override
    public UserDto register(UserDto userDto)
    {
        //Check duplicated email and valid password
        if (userRepository.existsByEmail(userDto.getEmail()) ) throw new DuplicateEntryException("Email has been used!");
        if (!checkPasswordInput(userDto.getPassword()))  throw new BadInputException("Password must contain at least 7 characters, one uppercase letter, one lowercase letter, one number, one special character (!@*/...) and no whitespace!");


        UserDto newUser = new UserDto(userDto.getId(), userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()),true, roleRepository.findById(0L).get());
        User savedUser = userRepository.save(UserMapper.mapToUser(newUser));
        return UserMapper.mapToUserDto(savedUser);
    }

    @Override
    public Boolean checkRoleInput(Long roleId)
    {
        UserDetails userDetails = null;
        try {
            userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (ClassCastException e) {
            throw new ResourceNotFoundException("Please logged in to use our services!");
        }
        User loggedInUser = userRepository.findByEmail(userDetails.getUsername());


        return roleId <= loggedInUser.getRole().getId();
    }

    @Override
    public UserDto createNewUser(UserDto userDto)
    {
        //Check duplicated email,valid password and role
        if (userRepository.existsByEmail(userDto.getEmail()) ) throw new DuplicateEntryException("Email has been used!");
        if (!checkPasswordInput(userDto.getPassword()))  throw new BadInputException("Password must contain at least 7 characters, one uppercase letter, one lowercase letter, one number, one special character (!@*/...) and no whitespace!");
        if (!checkRoleInput(userDto.getRole().getId())) throw new BadInputException("You don't have enough permission for creating user with this role!");
        Role gettedRole = roleRepository.findById(userDto.getRole().getId()).orElseThrow(()->new ResourceNotFoundException("Role is not exists"));

        UserDto newUser = new UserDto(userDto.getId(), userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()),true, gettedRole);
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
    public RoleDto getUserRolesByID(Long userID) {
        User gettedUser = userRepository.findById(userID)
                .orElseThrow( ()->new ResourceNotFoundException("User is not exists with given ID: " +userID) );
        return RoleMapper.mapToRoleDto(gettedUser.getRole());
    }
    @Override
    public Page<UserDto> getAllUser(Pageable pageable) {
        Page<User> userList = userRepository.findAll(pageable);
        Page<UserDto> result = userList.map((User) -> UserMapper.mapToUserDto(User));
        return result;
    }

    @Override
    public Page<UserDto> getAllUserWithCase(Pageable pageable) {

        //Get user logged-in ID
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



        Page<User> userList = userRepository.findAllWithCase(gettedLoggedInUserDto.getId() ,pageable);
        Page<UserDto> result = userList.map((User) -> UserMapper.mapToUserDto(User));
        return result;
    }



    @Override
    public List<UserDto> getUserByIDLike(Long id) {
        List<User> userList = userRepository.findByIdLike(id.toString());
        return userList.stream()
                .map( (User) -> UserMapper.mapToUserDto(User) )
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getUserByEmailLike(String email) {
        List<User> userList = userRepository.findByEmailLike(email);
        return userList.stream()
                .map( (User) -> UserMapper.mapToUserDto(User) )
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getUserByRoleNameLike(String roleName) {
        List<User> userList = userRepository.findByroleNameLike(roleName);
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


        gettedUser.setPassword(passwordEncoder.encode(password.trim()));

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


        //Valid the user role, ID of logged-in user vs ID of the user's being updated
        UserDetails userDetails = null;
        try {
            userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (ClassCastException e) {
            throw new ResourceNotFoundException("Please logged in to use our services!");
        }

        //Get user logged-in information
        User loggedInUser = userRepository.findByEmail(userDetails.getUsername());
        String roleNow = loggedInUser.getRole().getName();
        Integer idNow = loggedInUser.getId();
        String emailNow = loggedInUser.getEmail();

        //Only ROLE_ADMIN can change other user information but email
        if (Objects.equals(roleNow, "ROLE_ACCOUNT_MANAGER") || Objects.equals(roleNow, "ROLE_USER"))
        {
            if (!userID.equals(idNow.longValue())) throw new PermissionException("You don't have enough permission to this!");
        }

        //Check duplicated email when changing email (allow user to use their email again due to the use API in front-end)

        //If ADMIN is changing information
        if (Objects.equals(roleNow, "ROLE_ADMIN"))
        {
            //If ADMIN is changing other user information
            if (!userID.equals(idNow.longValue()))
            {
                if (userRepository.existsByEmail(updatedUser.getEmail()) && !Objects.equals(gettedUser.getEmail(), updatedUser.getEmail())) throw new DuplicateEntryException("Email has been used!");
            } //If ADMIN is changing self information
            else if (userRepository.existsByEmail(updatedUser.getEmail()) && !Objects.equals(emailNow, updatedUser.getEmail())) throw new DuplicateEntryException("Email has been used!");
        } // If not admin is changing
        else if (userRepository.existsByEmail(updatedUser.getEmail()) && !Objects.equals(emailNow, updatedUser.getEmail())) throw new DuplicateEntryException("Email has been used!");


        gettedUser.setFirstName(updatedUser.getFirstName().trim());
        gettedUser.setLastName(updatedUser.getLastName().trim());
        gettedUser.setEmail(updatedUser.getEmail().trim());

        User updatedUserObj = null;
        try {
            updatedUserObj = userRepository.save(gettedUser);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
        }

        return UserMapper.mapToUserDto(updatedUserObj);
    }

    @Override
    public UserDto updateUserRole(Long userID, UserDto updatedUser)
    {
        User gettedUser = userRepository.findById(userID)
                .orElseThrow( ()-> new ResourceNotFoundException("User is not exists with given ID: " +userID) );

        UserDetails userDetails = null;
        try {
            userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (ClassCastException e) {
            throw new ResourceNotFoundException("Please logged in to use our services!");
        }

        //Check valid role
        if (!checkRoleInput(updatedUser.getRole().getId())) throw new BadInputException("You don't have enough permission for changinig user to this role!");


        gettedUser.setRole(roleRepository.findById(updatedUser.getRole().getId()).orElseThrow(()->new ResourceNotFoundException("Role is not exists")));

        User updatedUserObj = null;
        try {
            updatedUserObj = userRepository.save(gettedUser);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
        }

        return UserMapper.mapToUserDto(updatedUserObj);
    }

    @Override
    public UserDto disableUser(Long userID)
    {
        User gettedUser = userRepository.findById(userID)
                .orElseThrow( ()-> new ResourceNotFoundException("User is not exists with given ID: " +userID) );

        UserDetails userDetails = null;
        try {
            userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (ClassCastException e) {
            throw new ResourceNotFoundException("Please logged in to use our services!");
        }

        User userNow = userRepository.findByEmail(userDetails.getUsername());
        Integer idNow = userNow.getId();
        Long roleNow = userNow.getRole().getId();
        Long roleChanged = gettedUser.getRole().getId();

        //Check if disable self account and same role account
        if (userID.equals(idNow.longValue())) throw new BadInputException("You cannot disable your account! Please contact admin or other account that has permission to do this!");
        if (roleChanged >= roleNow) throw new BadInputException("You don't have enough permission to do this!");

        gettedUser.setEnabled(!gettedUser.isEnabled());

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
