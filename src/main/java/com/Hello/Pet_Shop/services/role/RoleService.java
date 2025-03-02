package com.Hello.Pet_Shop.services.role;

import com.Hello.Pet_Shop.dto.RoleDto;

import java.util.List;

public interface RoleService
{
    RoleDto getRoleByRoleName(String roleName);

    RoleDto getRoleByID(Long roleID);

    List<RoleDto> getRoleHasIdSmallerOrEqual(Long roleID);

    List<RoleDto> getAllRoles();

}
