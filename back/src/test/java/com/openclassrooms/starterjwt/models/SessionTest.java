package com.openclassrooms.starterjwt.models;

import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class SessionTest {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    private Session session;
    private Teacher teacher;
    private User user1;
    private User user2;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void testInvalidSession() {
        Session session1 = new Session();
        session1.setDate(new Date());
        session1.setDescription("Description de la session de test");
        session1.setUpdatedAt(LocalDateTime.now());

        Set<ConstraintViolation<Session>> violations = validator.validate(session1);
        assertThat(violations).hasSize(1);
    }

    @BeforeEach
    public void setUp() {
        // Créer un enseignant
        teacher = new Teacher();
        teacher.setFirstName("Alan");
        teacher.setLastName("Turing");
        teacher = teacherRepository.save(teacher);

        // Créer des utilisateurs
        user1 = new User();
        user1.setFirstName("toto");
        user1.setLastName("tutu");
        user1.setEmail("toto@example.com");
        user1.setPassword("mdp");
        user1.setAdmin(false);
        user1 = userRepository.save(user1);

        user2 = new User();
        user2.setFirstName("Bob");
        user2.setLastName("leponge");
        user2.setEmail("bob@example.com");
        user2.setPassword("mdp2");
        user2.setAdmin(false);
        user2 = userRepository.save(user2);

        // Créer une session avec l'enseignant et les utilisateurs
        session = new Session();
        session.setName("Test Session");
        session.setDate(new Date());
        session.setDescription("Description de la session de test");
        session.setTeacher(teacher);
        session.setUsers(Arrays.asList(user1, user2));
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());
        session = sessionRepository.save(session);
    }

    @Test
    public void testValidSession() {
        Set<ConstraintViolation<Session>> violations = validator.validate(session);
        assertThat(violations).isEmpty();
    }

    @AfterEach
    public void tearDown() {
        sessionRepository.deleteAll();
        userRepository.deleteAll();
        teacherRepository.deleteAll();
    }

    @Test
    public void testFindAll() {
        List<Session> sessions = sessionRepository.findAll();
        assertEquals(1, sessions.size());
        assertEquals(session, sessions.get(0));
    }

    @Test
    public void testFindById() {
        Session foundSession = sessionRepository.findById(session.getId()).orElse(null);
        assertNotNull(foundSession);
        assertEquals(session, foundSession);
    }
    @Test
    @DirtiesContext
    public void testSave() {
        Session newSession = new Session();
        newSession.setName("cours d'alchimie");
        newSession.setDate(new Date());
        newSession.setDescription("cours de comprehension de la nature et ces éléments");
        newSession.setTeacher(teacher);
        newSession.setUsers(Arrays.asList(user1));
        newSession.setCreatedAt(LocalDateTime.now());
        newSession.setUpdatedAt(LocalDateTime.now());

        Session newSession2 = new Session(Long.parseLong("6"),"cours 2",new Date(),"cours2 description", teacher,Arrays.asList(user1),LocalDateTime.now(),LocalDateTime.now() );
        Session newSession3 = Session.builder()
                .id(Long.parseLong("7"))
                .name("cours 3")
                .date(new Date())
                .description("cours3 description")
                .teacher(teacher)
                .users(Arrays.asList(user1))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Session savedSession = sessionRepository.save(newSession);
        Session savedSession2 = sessionRepository.save(newSession2);
        Session savedSession3 = sessionRepository.save(newSession3);

        assertNotNull(savedSession.getId());
        assertEquals(newSession.getName(), savedSession.getName());
        assertEquals(newSession2.getName(), savedSession2.getName());
        assertEquals(newSession.getDescription(), savedSession.getDescription());
        assertEquals(newSession.getTeacher(), savedSession.getTeacher());
        assertEquals(newSession.getUsers(), savedSession.getUsers());

        assertEquals(newSession3.getName(), savedSession3.getName());
        assertEquals(newSession3.getDate(), savedSession3.getDate());
        assertEquals(newSession3.getDescription(), savedSession3.getDescription());
        assertEquals(newSession3.getUsers(), savedSession3.getUsers());
        assertEquals(newSession3.getTeacher(), savedSession3.getTeacher());
        assertEquals(newSession3.getCreatedAt().toString().substring(0,10), savedSession3.getCreatedAt().toString().substring(0,10));
        assertEquals(newSession3.getUpdatedAt().toString().substring(0,10), savedSession3.getUpdatedAt().toString().substring(0,10));

        assertEquals(newSession.toString().substring(0,10), savedSession.toString().substring(0,10));
        assertEquals(savedSession.hashCode(), newSession.hashCode());
        assertTrue(savedSession.equals(newSession));
    }

    @Test
    public void testDelete() {
        sessionRepository.delete(session);
        assertFalse(sessionRepository.existsById(session.getId()));
    }

    @Test
    public void TestBuilder(){

        LocalDateTime timeNow =LocalDateTime.now();

        String session1 = Session.builder()
                .id(Long.parseLong("7"))
                .name("cours 3")
                .date(new Date())
                .description("cours3 description")
                .teacher(teacher)
                .users(Arrays.asList(user1))
                .createdAt(timeNow)
                .updatedAt(timeNow)
                .toString();

        String session2 = Session.builder()
                .id(Long.parseLong("7"))
                .name("cours 3")
                .date(new Date())
                .description("cours3 description")
                .teacher(teacher)
                .users(Arrays.asList(user1))
                .createdAt(timeNow)
                .updatedAt(timeNow)
                .toString();

        assertTrue(session1.equals(session2));
    }
}