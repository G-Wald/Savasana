package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import lombok.var;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
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

    User CreateAndSaveUser(int number){
        User user = new User();
        user.setEmail(String.format("toto%1d@gmail.com", number));
        user.setPassword(String.format("tutu%1d", number));
        userRepository.save(user);
        return user;
    }

    Session CreateSession(int number){
        //Save a user
        User user = CreateAndSaveUser(number);
        Teacher teacher = new Teacher();
        teacher.setFirstName(String.format("Eikichi%1d", number));
        teacher.setLastName(String.format("Onizuka%1d", number));
        teacherRepository.save(teacher);
        Session session = new Session();
        session.setName(String.format("toto%1d", number));
        session.setTeacher(teacher);
        session.setDate(new Date(2024,Calendar.JANUARY,11));
        session.setDescription(String.format("Cours de philo%1d", number));
        var users = new ArrayList<User>();
        users.add(user);
        session.setUsers(users);
        session.setId(Long.valueOf(String.format("%1d", number)));
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
    @DirtiesContext
    void findById() {
        Session session = CreateSession(1);
        SessionDto sessionDto = ToDTO(session);
        //Cas sans session
        var response = sessionController.findById(session.getId().toString());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        //Cas nominal
        sessionRepository.save(session);

        Mockito.when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        response = sessionController.findById(session.getId().toString());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("toto1", ((SessionDto)response.getBody()).getName());
    }

    @Test
    @DirtiesContext
    void findAll() {
        //Creation des sessions
        Session session1 = CreateSession(1);
        Session session2 = CreateSession(2);
        Session session3 = CreateSession(3);
        SessionDto sessionDto1 = ToDTO(session1);
        SessionDto sessionDto2 = ToDTO(session2);
        SessionDto sessionDto3 = ToDTO(session3);
        List<Session> sessions = new ArrayList<>();
        List<SessionDto> sessionsDto = new ArrayList<>();
        sessions.add(session1);
        sessions.add(session2);
        sessions.add(session3);
        sessionsDto.add(sessionDto1);
        sessionsDto.add(sessionDto2);
        sessionsDto.add(sessionDto3);
        //Cas sans session
        var response = sessionController.findAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new ArrayList<SessionDto>(),response.getBody());

        //Cas nominal
        sessionRepository.save(session1);
        sessionRepository.save(session2);
        sessionRepository.save(session3);

        Mockito.when(sessionMapper.toDto(ArgumentMatchers.anyList())).thenReturn(sessionsDto);

        response = sessionController.findAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("toto1", ((ArrayList<SessionDto>)response.getBody()).get(0).getName());
        assertEquals(3, ((ArrayList<SessionDto>)response.getBody()).size());
    }

    @Test
    @DirtiesContext
    void create() {

        //Creation des sessions
        Session session1 = CreateSession(1);
        SessionDto sessionDto1 = ToDTO(session1);

        Mockito.when(sessionMapper.toEntity(sessionDto1)).thenReturn(session1);
        Mockito.when(sessionMapper.toDto(ArgumentMatchers.any(Session.class))).thenReturn(sessionDto1);

        //Cas nominal
        var response = sessionController.create(sessionDto1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("toto1", ((SessionDto)response.getBody()).getName());

        //Check if session is in Db
        var resultSession = sessionRepository.findAll();
        assertEquals(1, resultSession.size());
        assertEquals(session1.getName(), resultSession.get(0).getName());
    }

    @Test
    @DirtiesContext
    void update() {
        //Creation des sessions
        Session session1 = CreateSession(1);
        Session session2 = CreateSession(2);
        SessionDto sessionDto2 = ToDTO(session2);
        sessionRepository.save(session1);

        Mockito.when(sessionMapper.toEntity(sessionDto2)).thenReturn(session2);
        Mockito.when(sessionMapper.toDto(ArgumentMatchers.any(Session.class))).thenReturn(sessionDto2);

        //Check if session1 is in Db
        var sessions = sessionRepository.findAll();
        assertEquals(1,sessions.size());
        assertEquals("toto1",sessions.get(0).getName());

        //Cas nominal
        var testid = session1.getId().toString();
        var response = sessionController.update(session1.getId().toString(),sessionDto2);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("toto2", ((SessionDto)response.getBody()).getName());

        //Check if update session is in Db
        sessions = sessionRepository.findAll();
        //assertEquals(1,sessions.size());
        assertEquals("toto2",sessions.get(0).getName());
    }

    @Test
    @DirtiesContext
    void save() {
        //Creation des sessions
        Session session1 = CreateSession(1);
        sessionRepository.save(session1);

        //Cas id not found
        var response = sessionController.save("62");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        //Check if session1 is in Db
        var users = sessionRepository.findAll();
        assertEquals(1,users.size());
        assertEquals("toto1",users.get(0).getName());

        //Cas nominal
        response = sessionController.save(session1.getId().toString());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

        //Check if update session is in Db
        users = sessionRepository.findAll();
        assertEquals(0,users.size());
    }

    @Test
    @DirtiesContext
    void participate() {

        //Creation des sessions
        Session session1 = CreateSession(1);
        sessionRepository.save(session1);

        //Cas user n'existe pas
        Assertions.assertThrows(NotFoundException.class, () -> {
            sessionController.participate(session1.getId().toString(),"68");
        });

        //Check si session à un seul user
        var sessionResult = sessionRepository.findById(session1.getId());
        assertEquals(1,sessionResult.get().getUsers().size());
        assertEquals("tutu1",sessionResult.get().getUsers().get(0).getPassword());

        //Cas user n'existe pas
        var user = CreateAndSaveUser(2);
        Assertions.assertThrows(NotFoundException.class, () -> {
            sessionController.participate("69",user.getId().toString());
        });

        //Cas nominal
        var response = sessionController.participate(session1.getId().toString(),user.getId().toString());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

        //Check if 2 users in session
        sessionResult = sessionRepository.findById(session1.getId());
        assertEquals(2,sessionResult.get().getUsers().size());
        assertEquals("tutu2",sessionResult.get().getUsers().get(1).getPassword());

        //Cas user participe deja
        Assertions.assertThrows(BadRequestException.class, () -> {
            sessionController.participate(session1.getId().toString(),user.getId().toString());
        });
    }

    @Test
    @DirtiesContext
    void noLongerParticipate() {
        //Creation des sessions
        Session session1 = CreateSession(1);
        sessionRepository.save(session1);

        //Cas session n'existe pas
        var user = CreateAndSaveUser(2);
        Assertions.assertThrows(NotFoundException.class, () -> {
            sessionController.noLongerParticipate("69",user.getId().toString());
        });

        //Cas user ne participe pas
        Assertions.assertThrows(BadRequestException.class, () -> {
            sessionController.noLongerParticipate(session1.getId().toString(),user.getId().toString());
        });

        //Ajout participation
        var response = sessionController.participate(session1.getId().toString(),user.getId().toString());

        //Check if 2 users in session
        var sessionResult = sessionRepository.findById(session1.getId());
        assertEquals(2,sessionResult.get().getUsers().size());
        assertEquals("tutu2",sessionResult.get().getUsers().get(1).getPassword());

        //Cas nominal
        response = sessionController.noLongerParticipate(session1.getId().toString(),user.getId().toString());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

        //Check if 1 user in session
        sessionResult = sessionRepository.findById(session1.getId());
        assertEquals(1,sessionResult.get().getUsers().size());
        assertEquals("tutu1",sessionResult.get().getUsers().get(0).getPassword());
    }
}