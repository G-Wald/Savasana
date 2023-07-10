package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.SessionService;
import com.openclassrooms.starterjwt.services.UserService;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class SessionControllerTest {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    private SessionController sessionController;
    private SessionMapper sessionMapper;
    private SessionService sessionService;

    @BeforeEach
    public void setup() {
        sessionMapper = Mockito.mock(SessionMapper.class);
        sessionService = new SessionService(sessionRepository,userRepository);
        sessionController = new SessionController( sessionService, sessionMapper);
    }

    Session CreateSession(){
        //Save a user
        User user = new User();
        user.setEmail("toto@gmail.com");
        user.setPassword("tutu");
        userRepository.save(user);

        Teacher teacher = new Teacher();
        teacher.setFirstName("Eikichi");
        teacher.setLastName("Onizuka");
        teacherRepository.save(teacher);

        Session session = new Session();
        session.setName("toto");
        session.setTeacher(teacher);
        session.setDate(new Date(2024,02,11));
        session.setDescription("Cours de philo");
        var users = new ArrayList<User>();
        users.add(user);
        session.setUsers(users);
        session.setId(Long.valueOf("1"));
        return session;
    }

    SessionDto ToDTO(Session session){
        if ( session == null ) {
            return null;
        }

        SessionDto sessionDto = new SessionDto();

        sessionDto.setDescription( session.getDescription() );
        sessionDto.setTeacher_id( session.getTeacher().getId() );
        sessionDto.setId( session.getId() );
        sessionDto.setName( session.getName() );
        sessionDto.setDate( session.getDate() );
        sessionDto.setCreatedAt( session.getCreatedAt() );
        sessionDto.setUpdatedAt( session.getUpdatedAt() );

        sessionDto.setUsers( Optional.ofNullable(session.getUsers()).orElseGet(Collections::emptyList).stream().map(User::getId).collect(Collectors.toList()) );

        return sessionDto;
    }

    @Test
    void findById() {
        Session session = CreateSession();
        SessionDto sessionDto = ToDTO(session);
        //Cas sans session
        var response = sessionController.findById(session.getId().toString());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        //Cas avec session
        sessionRepository.save(session);
        //Check if user in db
        var users = userRepository.findAll();
        assertEquals("toto@gmail.com",users.get(0).getEmail());

        Mockito.when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        response = sessionController.findById(session.getId().toString());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("toto", ((SessionDto)response.getBody()).getName());

    }

    @Test
    void findAll() {
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void save() {
    }

    @Test
    void participate() {
    }

    @Test
    void noLongerParticipate() {
    }
}