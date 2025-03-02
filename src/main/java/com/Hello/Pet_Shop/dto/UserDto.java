package com.Hello.Pet_Shop.dto;

import com.Hello.Pet_Shop.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto
{

    public interface PasswordValidationGroup {}
    public interface RoleValidationGroup {}


    private Integer id;

    @NotBlank(message = "First name is required", groups = {Default.class})
    private String firstName;

    @NotBlank(message = "Last name is required", groups = {Default.class})
    private String lastName;

    @NotBlank(message = "Email is required", groups = {Default.class})
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Invalid email"
    )
    private String email;

    @NotBlank(message = "Password is required", groups = {PasswordValidationGroup.class})
    private String password;

    private boolean enable;

    @NotNull(message = "Role is required", groups = {RoleValidationGroup.class})
    private Role role;
}
