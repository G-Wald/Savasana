package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.services.UserService;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockitoSession;
import static org.mockito.Mockito.when;

//@DataJpaTest
@ExtendWith(MockitoExtension.class)
//@ContextConfiguration(classes = { SpringTestConfiguration.class })
class UserControllerTest{

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @Autowired
    private UserMapper userMapper;

    @Mock
    private UserService userService;


    @BeforeEach
    void setUpTest(){
        MockitoAnnotations.initMocks(this);
        //userRepository = Mockito.mock(UserRepository.class);
        //userMapper = Mockito.mock(UserMapper.class);
       //userService = new UserService(userRepository);
        //userController = new UserController( userService, userMapper);
    }

    @Test
    void testUserDb() {
        User user = new User();
        user.setPassword("toto");
        user.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        userRepository.save(user);

        ResponseEntity<?> response = userController.save(user.getId().toString());
        UserDto userDto = new UserDto();
        userDto.setPassword("toto");
        userDto.setId(user.getId());
        assertEquals(userDto ,response.getBody());
    }

    @Test
    void UserFindByIdTestDb() {
        User user = new User();
        user.setPassword("toto");
        user.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        Optional<User> optUser = Optional.ofNullable(user);
        Mockito.when(userRepository.findById(anyLong())).thenReturn(optUser);
        //Mockito.when(userService.findById(user.getId())).thenReturn(user);

        ResponseEntity<?> response = userController.findById(user.getId().toString());
        UserDto userDto = new UserDto();
        userDto.setPassword("toto");
        userDto.setId(user.getId());
        assertEquals(userDto ,response.getBody());
    }
    @Test
    void findById() {
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
    void save() {
    }
}