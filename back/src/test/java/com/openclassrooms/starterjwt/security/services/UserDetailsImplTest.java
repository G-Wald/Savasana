package com.openclassrooms.starterjwt.security.services;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collection;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserDetailsImplTest {

    private UserDetailsImpl userDetails;

    @BeforeEach
    public void setUp() {
        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("john.doe")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("password")
                .build();
    }

    @Test
    public void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    public void testIsAccountNonExpired() {
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    public void testIsAccountNonLocked() {
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    public void testIsCredentialsNonExpired() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    public void testIsEnabled() {
        assertTrue(userDetails.isEnabled());
    }

    @Test
    public void testEquals_SameObject() {
        assertTrue(userDetails.equals(userDetails));
    }

    @Test
    public void testEquals_EqualObjects() {
        UserDetailsImpl otherUser = UserDetailsImpl.builder()
                .id(1L)
                .username("john.doe")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("password")
                .build();

        assertTrue(userDetails.equals(otherUser));
    }

    @Test
    public void testEquals_DifferentObjects() {
        UserDetailsImpl otherUser = UserDetailsImpl.builder()
                .id(2L)
                .username("jane.doe")
                .firstName("Jane")
                .lastName("Doe")
                .admin(false)
                .password("password")
                .build();

        assertFalse(userDetails.equals(otherUser));
    }
}