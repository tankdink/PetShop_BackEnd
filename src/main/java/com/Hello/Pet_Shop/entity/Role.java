package com.Hello.Pet_Shop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="role")
public class Role
{
    @Id
    private Long role_id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description")
    private String description;
}


