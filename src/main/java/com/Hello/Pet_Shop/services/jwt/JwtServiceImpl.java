package com.Hello.Pet_Shop.services.jwt;

import com.Hello.Pet_Shop.entity.User;
import com.Hello.Pet_Shop.exceptions.ResourceNotFoundException;
import com.Hello.Pet_Shop.services.security.UserDetailsServicesImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService
{
    @Value("${JWT_SECRET_KEY}")
    private String SECRET_KEY;

    private final UserDetailsServicesImpl userDetailsServicesImpl;

    @Override
    public Boolean validateToken(String token, String email) //...
    {
        final String emailOfToken = extractEmail(token);
        return (email.equals(emailOfToken)) && !isTokenExpired(token);
    }

    private Boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }
    @Override
    public String extractEmail(String token)
    {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Date extractExpiration(String token)
    {
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
    {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token)
    {
        return Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
    }

    @Override
    public String generateToken(String email)
    {
        Map<String, Object> claims = new HashMap<>();
        User user = userDetailsServicesImpl.getUserByEmail(email);
        if (user != null)
        {
            claims.put("id", user.getId());
            claims.put("firstName", user.getFirstName());
            claims.put("lastName", user.getLastName());
            claims.put("email", user.getEmail());
            claims.put("role", user.getRole().getRole_id());
        }
        else throw new ResourceNotFoundException("User is not exists with given email: " +email);
        return createToken(claims, email);
    }

    private String createToken(Map<String, Object> claims, String subject)
    {
        return Jwts.builder().setClaims(claims)
                             .setSubject(subject)
                             .setIssuedAt(new Date(System.currentTimeMillis()))
                             .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 1))
                             .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }
}
