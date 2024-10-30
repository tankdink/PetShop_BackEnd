package com.Hello.Pet_Shop.services.role;

import com.Hello.Pet_Shop.entity.Role;
import com.Hello.Pet_Shop.repository.RoleRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Data
public class RoleServiceImpl implements RoleServices
{
    private final RoleRepository roleRepository;

    @Override
    public Role findRoleByRoleName(String roleName) {
        return null;
    }

    public Role findRoleByRoleNames(String roleName)
    {
        return roleRepository.findByName(roleName);
    }
}
