package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testToEntity() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("alan@gmail.com");
        userDto.setFirstName("alan");
        userDto.setLastName("Turing");
        userDto.setPassword("Pwd");

        User user = userMapper.toEntity(userDto);

        assertEquals(1L, user.getId());
        assertEquals("alan@gmail.com", user.getEmail());
        assertEquals("alan", user.getFirstName());
        assertEquals("Turing", user.getLastName());
        assertEquals("Pwd", user.getPassword());

        List<UserDto> userDtoList = Arrays.asList(userDto);
        List<User> userList  = userMapper.toEntity(userDtoList);
        assertEquals(1L, userList.get(0).getId());
        assertEquals("alan@gmail.com", userList.get(0).getEmail());
        assertEquals("alan", userList.get(0).getFirstName());
        assertEquals("Turing", userList.get(0).getLastName());
        assertEquals("Pwd", userList.get(0).getPassword());

    }

    @Test
    public void testToDto() {
        User user = new User();
        user.setId(1L);
        user.setEmail("alan@gmail.com");
        user.setFirstName("Jack");
        user.setLastName("Sparrow");

        UserDto userDto = userMapper.toDto(user);

        assertEquals(1L, userDto.getId());
        assertEquals("alan@gmail.com", userDto.getEmail());
        assertEquals("Jack", userDto.getFirstName());
        assertEquals("Sparrow", userDto.getLastName());

        List<User> userList = Arrays.asList(user);
        List<UserDto> userDtoList = userMapper.toDto(userList);
        assertEquals(1L, userDtoList.get(0).getId());
        assertEquals("alan@gmail.com", userDtoList.get(0).getEmail());
        assertEquals("Jack", userDtoList.get(0).getFirstName());
        assertEquals("Sparrow", userDtoList.get(0).getLastName());
    }
}
