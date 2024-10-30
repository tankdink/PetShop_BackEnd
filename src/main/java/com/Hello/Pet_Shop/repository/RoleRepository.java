package com.Hello.Pet_Shop.repository;

import com.Hello.Pet_Shop.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long>
{
    Role findByName(String roleName);
}
