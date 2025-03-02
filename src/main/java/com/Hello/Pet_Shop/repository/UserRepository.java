package com.Hello.Pet_Shop.repository;

import com.Hello.Pet_Shop.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>
{


    @Query("SELECT u FROM User u ORDER BY CASE WHEN u.id = :userID THEN 0 ELSE 1 END")
    Page<User> findAllWithCase(Integer userID, Pageable pageable);

    User findByEmail(String email);

    List<User> findByroleName(String roleName);

    List<User> findByfirstName(String firstName);

    Boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE CAST(u.id AS string) LIKE CONCAT('%', :id, '%')")
    List<User> findByIdLike(String id);

    @Query("SELECT u FROM User u WHERE u.email LIKE CONCAT('%', :email, '%')")
    List<User> findByEmailLike(String email);

    @Query("SELECT u FROM User u WHERE u.role.name LIKE CONCAT('%', :roleName, '%')")
    List<User> findByroleNameLike(String roleName);
}