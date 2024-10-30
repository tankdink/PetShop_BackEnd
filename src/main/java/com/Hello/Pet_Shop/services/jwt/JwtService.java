package com.Hello.Pet_Shop.services.jwt;

import io.jsonwebtoken.Claims;

import java.util.Date;
import java.util.function.Function;

public interface JwtService
{
    public Boolean validateToken(String token, String email);
    public String extractEmail(String token);
    public Date extractExpiration(String token);
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    public String generateToken(String email);
}
