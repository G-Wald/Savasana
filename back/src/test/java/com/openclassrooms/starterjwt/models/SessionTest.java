package com.openclassrooms.starterjwt.models;

import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
    public void testSave() {
        Session newSession = new Session();
        newSession.setName("cours d'alchimie");
        newSession.setDate(new Date());
        newSession.setDescription("cours de comprehension de la nature et ces éléments");
        newSession.setTeacher(teacher);
        newSession.setUsers(Arrays.asList(user1));
        newSession.setCreatedAt(LocalDateTime.now());
        newSession.setUpdatedAt(LocalDateTime.now());

        Session savedSession = sessionRepository.save(newSession);
        assertNotNull(savedSession.getId());
        assertEquals(newSession.getName(), savedSession.getName());
        assertEquals(newSession.getDescription(), savedSession.getDescription());
        assertEquals(newSession.getTeacher(), savedSession.getTeacher());
        assertEquals(newSession.getUsers(), savedSession.getUsers());
    }

    @Test
    public void testDelete() {
        sessionRepository.delete(session);
        assertFalse(sessionRepository.existsById(session.getId()));
    }
}