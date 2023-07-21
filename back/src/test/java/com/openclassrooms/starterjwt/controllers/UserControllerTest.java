package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class UserControllerTest{

    @Autowired
    private UserRepository userRepository;
    private SecurityContextHolder securityContextHolder;
    private UserController userController;
    private UserMapper userMapper;
    private UserService userService;


    @BeforeEach
    public void setup() {
        userMapper = Mockito.mock(UserMapper.class);
        securityContextHolder = new SecurityContextHolder();
        userService = new UserService(userRepository);
        userController = new UserController( userService, userMapper);
    }

    public User CreateAndSaveuser(){
        //Save a user
        User user = new User();
        user.setFirstName("alan");
        user.setLastName("turing");
        user.setEmail("toto@gmail.com");
        user.setPassword("tutu");
        userRepository.save(user);
        return user;
    }
    public UserDto ToDTO(User user){
        if ( user == null ) {
            return null;
        }
        UserDto userDto = new UserDto();
        userDto.setId( user.getId() );
        userDto.setFirstName( user.getFirstName() );
        userDto.setLastName( user.getLastName());
        userDto.setCreatedAt( user.getCreatedAt() );
        userDto.setUpdatedAt( user.getUpdatedAt() );

        return userDto;
    }

    @Test
    @DirtiesContext
    void findById_Test() {
        //User not in db
        var response = userController.findById("1");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull( response.getBody());

        //Add user in db
        User user = CreateAndSaveuser();

        UserDto userDto = ToDTO(user);

        //Mock mapper
        Mockito.when(userMapper.toDto(user)).thenReturn(userDto);

        //User in db
        response = userController.findById(user.getId().toString());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("alan", ((UserDto)response.getBody()).getFirstName());
    }

    @Test
    @DirtiesContext
    void deleteUser_Test() {
        var response = userController.save("1");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull( response.getBody());

        //Add user in db
        User user = CreateAndSaveuser();

        //Check if in db
        var optUser = userRepository.findById(user.getId());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(optUser.isPresent());

        //Mock mapper
        UserDetailsImpl userDetails = new UserDetailsImpl(Long.valueOf("1"),user.getEmail(),"tutu","toto", true,"psw");
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //User in db
        response = userController.save(user.getId().toString());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

        //Check IF user delete in db
        var deleteOptUser = userRepository.findById(user.getId());
        assertFalse(deleteOptUser.isPresent());
    }




}