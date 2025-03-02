package com.Hello.Pet_Shop.controller;

import com.Hello.Pet_Shop.dto.RoleDto;
import com.Hello.Pet_Shop.services.role.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/role")
@AllArgsConstructor
public class RoleController
{
    private RoleService roleService;

    //Get roles by name
    @GetMapping("/getRoleByName/{name}")
    public ResponseEntity<RoleDto> getUserById(@PathVariable("name") String roleName)
    {
        RoleDto gettedRole = roleService.getRoleByRoleName(roleName);
        return ResponseEntity.ok(gettedRole);
    }


    //Get all roles
    @GetMapping("/getAllRoles")
    public ResponseEntity<List<RoleDto>> getAllRoles()
    {
        List<RoleDto> gettedRole = roleService.getAllRoles();
        return ResponseEntity.ok(gettedRole);
    }

    @GetMapping("/getRoleHasIdSmallerOrEqual/{id}")
    public ResponseEntity<List<RoleDto>> getRoleHasIdSmallerOrEqual(@PathVariable("id") Long roleID)
    {
        List<RoleDto> gettedRole = roleService.getRoleHasIdSmallerOrEqual(roleID);
        return ResponseEntity.ok(gettedRole);
    }
}
