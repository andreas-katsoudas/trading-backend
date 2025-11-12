package com.andreas.authservice.util;

import com.andreas.authservice.enums.Role;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtUtilTest {

    private JwtUtil jwtUtil;

    // Minimum 32-byte key for HS256
    private final String secret = "ThisIsASecretKeyForJwtTesting123456";
    private final long expirationMs = 3600000;

    @BeforeEach
    public void setUp() {
        jwtUtil = new JwtUtil();

        // Inject private fields manually (no Spring context)
        try {
            var secretField = JwtUtil.class.getDeclaredField("secret");
            secretField.setAccessible(true);
            secretField.set(jwtUtil, secret);

            var expirationField = JwtUtil.class.getDeclaredField("expirationMs");
            expirationField.setAccessible(true);
            expirationField.set(jwtUtil, expirationMs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void generateTokenTest(){
        String token = jwtUtil.generateToken("Andreas", Role.USER);
        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3, "Token should have 3 parts");
    }

    @Test
    public void extractClaimsTest(){
        String token = jwtUtil.generateToken("Andreas", Role.USER);
        Claims claims = jwtUtil.extractClaims(token);

        assertNotNull(claims);
        assertEquals("Andreas", claims.getSubject());
        assertEquals(Role.USER.toString(), claims.get("role"));
    }

    @Test
    public void extractClaimsThrowsExceptionTest(){
        String token = jwtUtil.generateToken("Andreas", Role.USER);

        String tamperedToken = token.substring(0, token.length() - 2) + "aa";

        assertThrows(Exception.class, () -> jwtUtil.extractClaims(tamperedToken));
    }

    @Test
    public void validateTokenTrueTest(){
        String token = jwtUtil.generateToken("Andreas", Role.USER);
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    public void validateTokenFalseTest(){
        String token = "invalid.token.value";
        assertFalse(jwtUtil.validateToken(token));
    }



}
