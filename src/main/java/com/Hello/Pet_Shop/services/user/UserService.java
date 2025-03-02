package com.Hello.Pet_Shop.services.user;

import com.Hello.Pet_Shop.dto.RoleDto;
import com.Hello.Pet_Shop.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService
{

    UserDto register(UserDto userDto);

    Boolean checkRoleInput(Long roleId);

    UserDto createNewUser(UserDto userDto);

    UserDto getUserByID(Long userID);

    List<UserDto> getUserByFirstName(String firstName);

    UserDto getUserByEmail(String email);

    RoleDto getUserRolesByID(Long userID);

    Page<UserDto> getAllUser(Pageable pageable);

    Page<UserDto> getAllUserWithCase(Pageable pageable);

    List<UserDto> getUserByIDLike(Long id);

    List<UserDto> getUserByEmailLike(String email);

    List<UserDto> getUserByRoleNameLike(String roleName);

    UserDto updateUserRole(Long userID, UserDto updatedUser);

    UserDto updateUser(Long userID, UserDto updatedUser);

    Boolean checkPasswordInput(String password);

    UserDto updatePassword(Long userID, String password);

    UserDto disableUser(Long userID);

    void deleteUserByID(Long userID);

    void deleteAllUser();

}
