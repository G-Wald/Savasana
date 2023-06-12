package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private LoginRequest loginRequest;
    @Mock
    private SignupRequest signupRequest;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private Authentication authentication;
    @Mock
    private UserDetailsImpl userDetailsImpl;
    private AuthController authController;

    @BeforeEach
    void setUp(){

        this.authenticationManager = Mockito.mock(AuthenticationManager.class);
        this.authentication = Mockito.mock(Authentication.class);
        this.userDetailsImpl = Mockito.mock(UserDetailsImpl.class);
        this.jwtUtils = new JwtUtils();
        this.passwordEncoder = Mockito.mock(PasswordEncoder.class);
        this.userRepository = Mockito.mock(UserRepository.class);
        this.authController = new AuthController(authenticationManager,
                 passwordEncoder,
                 jwtUtils,
                 userRepository) ;
    }


    @DisplayName("user account is valid")
    @Test
    void testLoginAuthSuccess(){
        when(loginRequest.getEmail()).thenReturn("toto@gmail.com");
        when(loginRequest.getPassword()).thenReturn("toto");
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())))
                .thenReturn(new UsernamePasswordAuthenticationToken(null,null));
        when(authentication.getPrincipal()).thenReturn(userDetailsImpl);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("token");


        //when(userDetailsImpl.getUsername()).thenReturn("toto@gmail.com");
        //when(userDetailsImpl.getPassword()).thenReturn("toto");

        //userDetailsImpl = new UserDetailsImpl(000001l,"toto","to","to",true,"toto");



        final ResponseEntity result = authController.authenticateUser(loginRequest);
        assertEquals(5, result.getBody());

    }

}
