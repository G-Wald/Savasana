package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadUserByUsername_UserFound() {
        String email = "toto@gmail.com";
        User user = new User(1L, email, "alan", "turing", "password", false, LocalDateTime.now(),LocalDateTime.now());
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals(user.getFirstName(), ((UserDetailsImpl) userDetails).getFirstName());
        assertEquals(user.getLastName(), ((UserDetailsImpl) userDetails).getLastName());
        assertEquals(user.getPassword(), userDetails.getPassword());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        String username = "unknown.user";
        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(username));
    }
}