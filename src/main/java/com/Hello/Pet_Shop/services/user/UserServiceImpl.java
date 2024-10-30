package com.Hello.Pet_Shop.services.user;

import com.Hello.Pet_Shop.dto.UserDto;
import com.Hello.Pet_Shop.entity.Role;
import com.Hello.Pet_Shop.entity.User;
import com.Hello.Pet_Shop.exceptions.ResourceNotFoundException;
import com.Hello.Pet_Shop.mapper.UserMapper;
import com.Hello.Pet_Shop.repository.RoleRepository;
import com.Hello.Pet_Shop.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public UserDto updateUser(Long userID, UserDto updatedUser)
    {
        User gettedUser = userRepository.findById(userID)
                .orElseThrow( ()-> new ResourceNotFoundException("User is not exists with given ID: " +userID) );

        gettedUser.setFirstName(updatedUser.getFirstName());
        gettedUser.setLastName(updatedUser.getLastName());
        gettedUser.setEmail(updatedUser.getEmail());

        User updatedUserObj = userRepository.save(gettedUser);
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
