package com.openclassrooms.starterjwt.controllers;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserControllerDbTest {

    @Autowired
    private UserRepository userRepository;

    private UserController userController;
    private UserMapper userMapper;
    private UserService userService;

    @BeforeEach
    public void setup() {
        //Save a user
        User user = new User();
        user.setEmail("toto@gmail.com");
        user.setPassword("tutu");
        user.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        userRepository.save(user);

        userMapper = Mockito.mock(UserMapper.class);
        userService = new UserService(userRepository);
        userController = new UserController( userService, userMapper);
    }

    @Test
    public void TestDbRepo() {
        //Check if user in db
        var users = userRepository.findAll();
        assertEquals("toto@gmail.com",users.get(0).getEmail());

        var user = users.get(0);
        var userDetails = new UserDetailsImpl(user.getId(), user.getEmail(),user.getFirstName(),user.getLastName(),true,user.getPassword());
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        //Mock l'authentification
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userDetails);

        ResponseEntity<?> response = userController.save(user.getId().toString());

        //Check IF user delete in db
        var deleteOptUser = userRepository.findById(users.get(0).getId());
        assertFalse(deleteOptUser.isPresent());
    }
}
