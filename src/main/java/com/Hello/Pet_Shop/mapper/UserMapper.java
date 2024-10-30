package com.Hello.Pet_Shop.mapper;

import com.Hello.Pet_Shop.dto.UserDto;
import com.Hello.Pet_Shop.entity.User;




public class UserMapper
{
    public static UserDto mapToUserDto(User user)
    {
        boolean isEnable = user.isEnabled();
        return new UserDto
        (
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPassword(),
            isEnable,
            user.getRole()
        );
    }

    public static User mapToUser(UserDto userDto)
    {
        boolean isEnable = userDto.isEnable();

        return new User
        (
            userDto.getId(),
            userDto.getFirstName(),
            userDto.getLastName(),
            userDto.getEmail(),
            userDto.getPassword(),
            isEnable,
            userDto.getRole()
        );
    }

    public static String convertRoleToUserDto(String roleName)
    {
        String[] parts = roleName.split("_");
        return parts[parts.length - 1];
    }

}
