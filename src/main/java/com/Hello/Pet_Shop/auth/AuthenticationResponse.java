package com.Hello.Pet_Shop.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponse
{
    private final String jwt;
}
