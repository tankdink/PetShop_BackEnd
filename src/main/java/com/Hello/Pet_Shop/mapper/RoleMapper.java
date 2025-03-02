package com.Hello.Pet_Shop.mapper;

import com.Hello.Pet_Shop.dto.RoleDto;
import com.Hello.Pet_Shop.entity.Role;

public class RoleMapper 
{
    public static RoleDto mapToRoleDto(Role role)
    {
        return new RoleDto
                (
                        role.getName(),
                        role.getId(),
                        role.getDescription()
                );
    }

    public static Role mapToRole(RoleDto roleDto)
    {

        return new Role
                (
                        roleDto.getId(),
                        roleDto.getName(),
                        roleDto.getDescription()
                );
    }
}
