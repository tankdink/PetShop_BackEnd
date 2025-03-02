package com.Hello.Pet_Shop.repository;

import com.Hello.Pet_Shop.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long>
{
    Role findByName(String roleName);

    List<Role> findByIdLessThanEqual(Long role_id);

}
