package com.Hello.Pet_Shop.services.role;

import com.Hello.Pet_Shop.dto.RoleDto;
import com.Hello.Pet_Shop.entity.Role;
import com.Hello.Pet_Shop.exceptions.ResourceNotFoundException;
import com.Hello.Pet_Shop.mapper.RoleMapper;
import com.Hello.Pet_Shop.repository.RoleRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Data
public class RoleServiceImpl implements RoleService
{
    private final RoleRepository roleRepository;

    @Override
    public RoleDto getRoleByRoleName(String roleName) {
        Role gettedRole = roleRepository.findByName(roleName);
        if (gettedRole == null) throw new ResourceNotFoundException("Role is not exists with given name: " + roleName);
        return RoleMapper.mapToRoleDto(gettedRole);
    }

    @Override
    public RoleDto getRoleByID(Long roleID) {
        Role gettedRole = roleRepository.findById(roleID)
                .orElseThrow( ()->new ResourceNotFoundException("Role is not exists with given ID: " + roleID) );;
        return RoleMapper.mapToRoleDto(gettedRole);
    }

    @Override
    public List<RoleDto> getRoleHasIdSmallerOrEqual(Long roleID) {
        List<Role> gettedRoleList = roleRepository.findByIdLessThanEqual(roleID);
        if (gettedRoleList.isEmpty()) throw new ResourceNotFoundException("No role is found for this!");
        return gettedRoleList.stream()
                .map( (Role) -> RoleMapper.mapToRoleDto(Role) )
                .collect(Collectors.toList());
    }

    @Override
    public List<RoleDto> getAllRoles() {
        List<Role> roleList = roleRepository.findAll();
        return roleList.stream()
                .map( (Role) -> RoleMapper.mapToRoleDto(Role) )
                .collect(Collectors.toList());
    }
}
