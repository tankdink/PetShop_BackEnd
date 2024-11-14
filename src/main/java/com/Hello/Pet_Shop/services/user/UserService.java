package com.Hello.Pet_Shop.services.user;

import com.Hello.Pet_Shop.dto.UserDto;
import com.Hello.Pet_Shop.entity.Role;

import java.util.List;

public interface UserService
{
    UserDto createNewUser(UserDto userDto);

    UserDto getUserByID(Long userID);

    List<UserDto> getUserByFirstName(String firstName);

    UserDto getUserByEmail(String email);

    Role getRolesByID(Long userID);

    List<UserDto> getAllUser();


    UserDto updateUser(Long userID, UserDto updatedUser);

    Boolean checkPasswordInput(String password);

    UserDto updatePassword(Long userID, String password);

    void deleteUserByID(Long userID);

    void deleteAllUser();

}
