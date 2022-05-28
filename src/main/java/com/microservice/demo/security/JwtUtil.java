package com.microservice.demo.security;

import com.microservice.demo.exception.JwtTokenMalformedException;
import com.microservice.demo.exception.JwtTokenMissingException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Optional;

/**
 * Created by: Tharindu Eranga
 * Date: 28 May 2022
 **/
@Slf4j
@Component
public class JwtUtil {

    private static final String EMPTY_SPACE = " ";

    @Value("${jwt.secret}")
    private String jwtSecret;

    public Optional<Claims> getClaims(final String token) {
        try {
            return Optional.ofNullable(parseClaims(token).getBody());
        } catch (Exception e) {
            log.error(e.getMessage() + " => " + e.getCause(), e);
        }
        return Optional.empty();
    }

    public void validateToken(final String token) throws JwtTokenMalformedException, JwtTokenMissingException {
        try {
            parseClaims(token);
        } catch (SignatureException ex) {
            throw new JwtTokenMalformedException("Invalid JWT signature");
        } catch (MalformedJwtException | ArrayIndexOutOfBoundsException ex) {
            throw new JwtTokenMalformedException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new JwtTokenMalformedException("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new JwtTokenMalformedException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new JwtTokenMissingException("JWT claims string is empty.");
        }
    }

    private Jws<Claims> parseClaims(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token.split(EMPTY_SPACE)[1]);
    }

}