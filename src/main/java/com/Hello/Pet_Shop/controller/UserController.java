package com.Hello.Pet_Shop.controller;

import com.Hello.Pet_Shop.dto.PageRequestDto;
import com.Hello.Pet_Shop.dto.RoleDto;
import com.Hello.Pet_Shop.dto.UserDto;
import com.Hello.Pet_Shop.services.user.UserService;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController
{
    private UserService userService;

    //Register new user
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Validated({Default.class, UserDto.PasswordValidationGroup.class}) @RequestBody UserDto userDto)
    {
        UserDto savedUser = userService.register(userDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
    //Add new User
    @PostMapping("/addUser")
    public ResponseEntity<UserDto> addUser(@Validated({Default.class, UserDto.PasswordValidationGroup.class, UserDto.RoleValidationGroup.class}) @RequestBody UserDto userDto)
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
    @GetMapping("/getUserRolesByID/{id}")
    public ResponseEntity<RoleDto> getRolesById(@PathVariable("id") Long userID)
    {
        RoleDto gettedRole = userService.getUserRolesByID(userID);
        return ResponseEntity.ok(gettedRole);
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
    public ResponseEntity<Page<UserDto>> getAllUser(@RequestParam(defaultValue = "0") Integer pageNo,
                                                    @RequestParam(defaultValue = "5") Integer pageSize)
    {
        Pageable pageable = new PageRequestDto().getPageable(pageNo, pageSize);
        Page<UserDto> userList = userService.getAllUser(pageable);
        return ResponseEntity.ok(userList);
    }

    //Get user with ID likely to
    @GetMapping("/getUserByIDLike/{id}")
    public ResponseEntity<List<UserDto>> getUserByIDLike(@PathVariable("id") Long id)
    {
        List<UserDto> userList = userService.getUserByIDLike(id);
        return ResponseEntity.ok(userList);
    }

    //Get user with email likely to
    @GetMapping("/getUserByEmailLike/{email}")
    public ResponseEntity<List<UserDto>> getUserByEmailLike(@PathVariable("email") String email)
    {
        List<UserDto> userList = userService.getUserByEmailLike(email);
        return ResponseEntity.ok(userList);
    }

    //Get user with roleName likely to
    @GetMapping("/getUserByRoleNameLike/{roleName}")
    public ResponseEntity<List<UserDto>> getUserByRoleNameLike(@PathVariable("roleName") String roleName)
    {
        List<UserDto> userList = userService.getUserByRoleNameLike(roleName);
        return ResponseEntity.ok(userList);
    }

    //Update User password with ID
    @PutMapping("/updateUserPassword/{id}")
    public ResponseEntity<UserDto> updateUserPassword(@PathVariable Long id,@Validated({Default.class, UserDto.PasswordValidationGroup.class}) @RequestBody UserDto userDto)
    {
        UserDto updatedUserObj = userService.updatePassword(id, userDto.getPassword());
        return ResponseEntity.ok(updatedUserObj);
    }

    //Update User with ID
    @PutMapping("/updateUser/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id,@Validated(Default.class) @RequestBody UserDto updatedUser)
    {
        UserDto updatedUserObj = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(updatedUserObj);
    }

    //Update User role with ID
    @PutMapping("/updateUserRole/{id}")
    public ResponseEntity<UserDto> updateUserRole(@PathVariable Long id, @Validated(UserDto.RoleValidationGroup.class) @RequestBody UserDto updatedUser)
    {
        UserDto updatedUserObj = userService.updateUserRole(id, updatedUser);
        return ResponseEntity.ok(updatedUserObj);
    }

    @PutMapping("/disable/{id}")
    public ResponseEntity<UserDto> updateUserRole(@PathVariable Long id)
    {
        UserDto updatedUserObj = userService.disableUser(id);
        return ResponseEntity.ok(updatedUserObj);
    }

    //Delete User with ID
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id)
    {

        try {
            userService.deleteUserByID(id);
        } catch (AccessDeniedException e) {
            throw new AccessDeniedException("???");
        }

        return ResponseEntity.ok("Deleted User with ID " + id + " successful!");
    }

    //Delete All User
    @DeleteMapping("/deleteAllUser")
    public ResponseEntity<String> deleteAllUser()
    {
        try {
            userService.deleteAllUser();
        } catch (Exception e) {
            throw new AccessDeniedException("???");
        }
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


    @GetMapping("/getAllUserWithCase")
    public ResponseEntity<Page<UserDto>> getAllUserTest(@RequestParam(defaultValue = "0") Integer pageNo,
                                                    @RequestParam(defaultValue = "5") Integer pageSize)
    {
        Pageable pageable = new PageRequestDto().getPageable(pageNo, pageSize);
        Page<UserDto> userList = userService.getAllUserWithCase(pageable);
        return ResponseEntity.ok(userList);
    }
}