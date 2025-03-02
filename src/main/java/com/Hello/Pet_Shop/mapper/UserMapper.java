package com.Hello.Pet_Shop.mapper;

import com.Hello.Pet_Shop.dto.UserDto;
import com.Hello.Pet_Shop.entity.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


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

        Date birthday = null;
        try {

            birthday = new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-01");
        } catch (ParseException e) {
            e.printStackTrace();

        }

        return new User
        (
            userDto.getId(),
            userDto.getFirstName(),
            userDto.getLastName(),
            userDto.getEmail(),
            userDto.getPassword(),
            isEnable,
            userDto.getRole(),birthday,"0328363762",true

        );
    }

    public static String convertRoleToUserDto(String roleName)
    {
        String[] parts = roleName.split("_");
        return parts[parts.length - 1];
    }

}
