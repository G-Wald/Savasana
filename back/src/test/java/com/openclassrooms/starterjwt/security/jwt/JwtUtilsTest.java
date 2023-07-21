package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtUtilsTest {

    @InjectMocks
    private JwtUtils jwtUtils;

    private Authentication authentication;

    private UserDetails userDetails;

    private final String jwtSecret = "jwt-secret";
    private final int jwtExpirationMs = 86400000;

    @BeforeEach
    public void setUp() {
        authentication = Mockito.mock(Authentication.class);
        userDetails = Mockito.mock(UserDetails.class);
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "jwt-secret");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 86400000);
    }

    @Test
    public void testGenerateJwtToken() {
        String username = "alan.turing";
        UserDetailsImpl userDetails1 = new UserDetailsImpl(Long.valueOf("1"),username,"tutu","toto", true,"psw");

        when(authentication.getPrincipal()).thenReturn(userDetails1);
        when(userDetails.getUsername()).thenReturn(username);

        String jwtToken = jwtUtils.generateJwtToken(authentication);

        assertNotNull(jwtToken);

        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken).getBody();
        assertEquals(username, claims.getSubject());
    }

    @Test
    public void testGetUserNameFromJwtToken() {
        String username = "alan.turing";
        String jwtToken = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        String extractedUsername = jwtUtils.getUserNameFromJwtToken(jwtToken);

        assertEquals(username, extractedUsername);
    }

    @Test
    public void testValidateJwtToken_ValidToken() {
        String jwtToken = Jwts.builder()
                .setSubject("alan.turing")
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        boolean isValid = jwtUtils.validateJwtToken(jwtToken);

        assertTrue(isValid);
    }

    @Test
    public void testValidateJwtToken_InvalidToken() {
        String invalidJwtToken = "invalid-jwt-token";

        boolean isValid = jwtUtils.validateJwtToken(invalidJwtToken);

        assertFalse(isValid);
    }
}