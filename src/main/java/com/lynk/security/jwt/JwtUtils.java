package com.lynk.security.jwt;

import com.lynk.service.UserDetailsImpl;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

public class JwtUtils
{

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpirationMs;


    // Authorization -> Bearer <TOKEN>
    public String getJwtFromHeader(HttpServletRequest request)
    {
        String bearerToken =request.getHeader("Authorization");

        if (bearerToken != null &&  bearerToken.startsWith("Bearer "))
        {
            return bearerToken.substring(7);
        }
        return null;
    }


    public String generateToken(UserDetailsImpl userDetails)
    {
        String username = userDetails.getUsername();
        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtExpirationMs)) // 2 days
                .signWith(getEncodedKey())
                .compact();
    }

    private Key getEncodedKey()
    {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromJwt(String token)
    {
        return Jwts.parser()
                .verifyWith((SecretKey) getEncodedKey())
                .build().parseEncryptedClaims(token)
                .getPayload().getSubject();
    }

    public boolean validateToken(String authToken)
    {
        try
        {
            Jwts.parser().verifyWith((SecretKey) getEncodedKey())
                    .build().parseSignedClaims(authToken);

            return true;
        }
        catch (JwtException e)
        {
            throw new RuntimeException(e);
        }
        catch (IllegalArgumentException e)
        {
            throw new RuntimeException(e);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    }


}
