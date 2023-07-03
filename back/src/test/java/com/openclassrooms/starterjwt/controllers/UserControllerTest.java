package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest{

    private UserRepository userRepository;
    private UserController userController;
    private UserMapper userMapper;
    private UserService userService;

    @BeforeEach
    void setUp(){
        userRepository = Mockito.mock(UserRepository.class);
        userMapper = Mockito.mock(UserMapper.class);
        userService = new UserService(userRepository);
        userController = new UserController( userService, userMapper);
    }
    @Test
    void findById_Test() {
        String id = "00000001";
        User user = new User();

        user.setId(Long.valueOf(id));
        user.setFirstName("Alan");
        user.setLastName("Turing");
        user.setPassword("compila");
        LocalDateTime localDate = LocalDateTime.now();
        user.setCreatedAt(localDate);
        user.setUpdatedAt(localDate);
        Optional<User> opt = Optional.ofNullable(user);

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPassword(user.getPassword());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setUpdatedAt(user.getUpdatedAt());
        when(userRepository.findById(Long.valueOf(id))).thenReturn(opt);
        when(userMapper.toDto(user)).thenReturn(userDto);
        var response = userController.findById(id);
        //Retour de données
        assertEquals( userDto,response.getBody());
        assertEquals(HttpHeaders.EMPTY,response.getHeaders());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    void deleteUser_Test() {
        User user = new User();
        user.setPassword("toto");
        user.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        Optional<User> optUser = Optional.ofNullable(user);
        var userDetails = new UserDetailsImpl(user.getId(), user.getEmail(),user.getFirstName(),user.getLastName(),true,user.getPassword());
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        //Supprimer les mock pour utiliser une database
        Mockito.when(userRepository.findById(anyLong())).thenReturn(optUser);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userDetails);

        ResponseEntity<?> response = userController.save(user.getId().toString());
        assertEquals(HttpHeaders.EMPTY,response.getHeaders());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}