package com.openclassrooms.starterjwt.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SessionMapperTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SessionMapper sessionMapper = Mappers.getMapper(SessionMapper.class);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testToEntity() {
        // Créer une SessionDto à mapper
        SessionDto sessionDto = new SessionDto();
        sessionDto.setDescription("Test Session");
        sessionDto.setTeacher_id(1L);
        List<Long> userIds = Collections.singletonList(2L);
        sessionDto.setUsers(userIds);

        Teacher teacher = new Teacher();
        teacher.setId(Long.parseLong("1"));
        // Configurer les comportements des mocks
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(2L)).thenReturn(new User());

        // Mapper la SessionDto en Session
        Session session = sessionMapper.toEntity(sessionDto);

        // Vérifier les mappages
        assertEquals("Test Session", session.getDescription());
        assertNotNull(session.getTeacher());
        assertEquals(1L, session.getTeacher().getId());
        assertEquals(1, session.getUsers().size());
    }

    @Test
    public void testToDto() {
        // Créer une Session à mapper
        Session session = new Session();
        session.setDescription("Test Session");
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        session.setTeacher(teacher);
        List<User> users = Collections.singletonList(new User());
        users.get(0).setId(2L);
        session.setUsers(users);

        // Mapper la Session en SessionDto
        SessionDto sessionDto = sessionMapper.toDto(session);

        // Vérifier les mappages
        assertEquals("Test Session", sessionDto.getDescription());
        assertEquals(1L, sessionDto.getTeacher_id());
        assertEquals(1, sessionDto.getUsers().size());
        assertEquals(2L, sessionDto.getUsers().get(0));
    }
}
