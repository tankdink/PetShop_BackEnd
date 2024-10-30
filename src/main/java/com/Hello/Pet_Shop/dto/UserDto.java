package com.Hello.Pet_Shop.dto;

import com.Hello.Pet_Shop.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto
{
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean enable;
    private Role role;
}
