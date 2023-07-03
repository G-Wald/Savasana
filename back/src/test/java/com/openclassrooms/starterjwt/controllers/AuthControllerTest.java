package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private UserRepository userRepository;
    private AuthController authController;

    private PasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;
    private AuthenticationManager authenticationManager;

    public String Email = "toto@gmail.com";

    public String Password = "tutu";
    public String FirstName = "Harlan";
    public String LastName = "Coben";

    @BeforeEach
    void setUp(){

        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationManager = new AuthenticationManager() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                return null;
            }
        };
        jwtUtils = new JwtUtils();

        authController = new AuthController(authenticationManager,
                passwordEncoder,
                jwtUtils,
                userRepository);
        //userController = new UserController( userService, userMapper);
        //this.authController = new AuthController(authenticationManager,                 passwordEncoder,                 jwtUtils,                 userRepository) ;
    }


    @DisplayName("Registration valid")
    @Test
    void RegisterAuthSuccess_test(){
        Mockito.when(passwordEncoder.encode(anyString())).thenReturn(Password);
        var signupRequest = new SignupRequest();
        signupRequest.setEmail(Email);
        signupRequest.setFirstName(FirstName);
        signupRequest.setLastName(LastName);
        signupRequest.setPassword(Password);
        final ResponseEntity response = authController.registerUser(signupRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        var userInDb = userRepository.findByEmail(Email);
        assertTrue( userInDb.isPresent());
    }

    @DisplayName("Registration invalid email already in database")
    @Test
    void RegisterAuthAlreadyInDb_test(){
        //Save user in Db
        User user = new User();
        user.setEmail(Email);
        user.setFirstName(FirstName);
        user.setLastName(LastName);
        user.setPassword(Password);
        user.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        userRepository.save(user);

        var signupRequest = new SignupRequest();
        signupRequest.setEmail(Email);
        signupRequest.setFirstName(FirstName);
        signupRequest.setLastName(LastName);
        signupRequest.setPassword(Password);
        final ResponseEntity response = authController.registerUser(signupRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Email is already taken!", ((MessageResponse)response.getBody()).getMessage());
    }
}
