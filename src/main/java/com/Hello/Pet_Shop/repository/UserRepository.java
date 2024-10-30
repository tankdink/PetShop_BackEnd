package com.Hello.Pet_Shop.repository;

import com.Hello.Pet_Shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>
{

    User findByEmail(String email);
    List<User> findByfirstName(String firstName);
}