package com.Hello.Pet_Shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto
{
    private String name;
    private Long id;
    private String description;
}
