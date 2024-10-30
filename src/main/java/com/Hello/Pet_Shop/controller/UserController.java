package com.Hello.Pet_Shop.controller;

import com.Hello.Pet_Shop.dto.UserDto;
import com.Hello.Pet_Shop.entity.Role;
import com.Hello.Pet_Shop.services.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController
{
    private UserService userService;

    //Add new User
    @PostMapping("/addUser")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto)
    {
        UserDto savedUser = userService.createNewUser(userDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    //Get 1 User by ID
    @GetMapping("/getUserByID/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long userID)
    {
        UserDto gettedUser = userService.getUserByID(userID);
        return ResponseEntity.ok(gettedUser);
    }

    //Get 1 user by email
    @GetMapping("/getUserByEmail/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable("email") String email)
    {
        UserDto gettedUser = userService.getUserByEmail(email);
        return ResponseEntity.ok(gettedUser);
    }

    //Get role of a user
    @GetMapping("/getRolesByID/{id}")
    public ResponseEntity<Role> getRolesById(@PathVariable("id") Long userID)
    {
        Role gettedUser = userService.getRolesByID(userID);
        return ResponseEntity.ok(gettedUser);
    }

    //Get User by firstName
    @GetMapping("/getUserByFirstName/{name}")
    public ResponseEntity<List<UserDto>> getUserByFirstName(@PathVariable("name") String firstName)
    {
        List<UserDto> gettedUserList = userService.getUserByFirstName(firstName);
        return ResponseEntity.ok(gettedUserList);
    }


    //Get all User
    @GetMapping("/getAllUser")
    public ResponseEntity<List<UserDto>> getAllUser()
    {
        List<UserDto> userList = userService.getAllUser();
        return ResponseEntity.ok(userList);
    }

    //Update User with ID
    @PutMapping("/updateUser/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id,@RequestBody UserDto updatedUser)
    {
        UserDto updatedUserObj = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(updatedUserObj);
    }

    //Delete User with ID
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id)
    {
        userService.deleteUserByID(id);
        return ResponseEntity.ok("Deleted User with ID " + id + " successful!");
    }

    //Delete All User
    @DeleteMapping("/deleteAllUser")
    public ResponseEntity<String> deleteAllUser()
    {
        userService.deleteAllUser();
        return ResponseEntity.ok("Deleted all User successful!");
    }

    @GetMapping("/user")
    public String hi_user()
    {
        return ("<h1>Welcome User</h1>");
    }

    @GetMapping("/admin")
    public String hi_admin()
    {
        return ("<h1>Welcome Admin</h1>");
    }
}