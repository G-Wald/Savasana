package com.openclassrooms.starterjwt.models;

import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    private UserRepository userRepository;


    public User CreateAndSaveUser(String id){
        User user = User.builder()
                .id(Long.parseLong(id))
                .email("email@gmail.com")
                .lastName("teach2")
                .firstName("prenom2")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .password("pwd")
                .admin(true)
                .build();
        return userRepository.save(user);
    };

    @Test
    public void testValidUser() {
        User user = new User ();
        user.setEmail("toto@example.com");
        user.setLastName("Turing");
        user.setFirstName("Alan");
        user.setPassword("tutu");
        user.setAdmin(false);

        User user2 = User.builder()
                .id(Long.parseLong("2"))
                .email("email2@gmail.com")
                .lastName("teach2")
                .firstName("prenom2")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .password("pwd")
                .admin(true)
                .build();

        User user3 = new User(Long.parseLong("3"),"email3@gmail.com","teach3", "prenom3","pwdtrreslong",true ,LocalDateTime.now(),LocalDateTime.now());
        User user4 = new User("email4@gmail.com","teach4", "prenom4","pwdtrreslong",true );
        user4.setId(Long.parseLong("4"));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).isEmpty();
        violations = validator.validate(user2);
        assertThat(violations).isEmpty();
        violations = validator.validate(user3);
        assertThat(violations).isEmpty();
        violations = validator.validate(user4);
        assertThat(violations).isEmpty();

        User savedUser = userRepository.save(user);
        User savedUser2 = userRepository.save(user2);
        User savedUser3 = userRepository.save(user3);
        User savedUser4 = userRepository.save(user4);
        assertNotNull(savedUser.getId());
        assertEquals("Alan", savedUser.getFirstName());
        assertEquals("Turing", savedUser.getLastName());
        assertNotNull(savedUser.getCreatedAt());

        assertNotNull(savedUser2.getId());
        assertEquals("prenom2", savedUser2.getFirstName());
        assertEquals("email2@gmail.com", savedUser2.getEmail());
        assertEquals("teach2", savedUser2.getLastName());
        assertEquals(LocalDateTime.now().toString().substring(0,10), savedUser2.getUpdatedAt().toString().substring(0,10));
        assertEquals(LocalDateTime.now().toString().substring(0,10), savedUser2.getCreatedAt().toString().substring(0,10));
        assertEquals("pwd", savedUser2.getPassword());
        assertNotNull(savedUser2.getCreatedAt());

        assertNotNull(savedUser3.getId());
        assertEquals("prenom3", savedUser3.getFirstName());
        assertEquals("teach3", savedUser3.getLastName());
        assertEquals("email3@gmail.com", savedUser3.getEmail());
        assertEquals("teach3", savedUser3.getLastName());
        assertEquals(LocalDateTime.now().toString().substring(0,10), savedUser3.getUpdatedAt().toString().substring(0,10));
        assertEquals(LocalDateTime.now().toString().substring(0,10), savedUser3.getCreatedAt().toString().substring(0,10));
        assertEquals("pwdtrreslong", savedUser3.getPassword());
        assertNotNull(savedUser3.getCreatedAt());

        savedUser.setFirstName("Jane");
        savedUser.setUpdatedAt(LocalDateTime.now());
        savedUser = userRepository.save(savedUser);

        assertEquals("Jane", savedUser.getFirstName());
        assertNotNull(savedUser.getUpdatedAt());
        assertEquals(savedUser.toString().substring(0,106),"User(id=1, email=toto@example.com, lastName=Turing, firstName=Jane, password=tutu, admin=false, createdAt=");

        assertEquals(user.toString().substring(0,106), savedUser.toString().substring(0,106));
        assertEquals(user2.toString().substring(0,106), savedUser2.toString().substring(0,106));
        assertEquals(user.hashCode(), savedUser.hashCode());
        assertEquals(user2.hashCode(), savedUser2.hashCode());
        assertEquals(user3.hashCode(), savedUser3.hashCode());
        assertEquals(user4.hashCode(), savedUser4.hashCode());
        assertTrue(user.equals(savedUser));
        assertTrue(user2.equals(savedUser2));
        assertTrue(user3.equals(savedUser3));
        assertTrue(user4.equals(savedUser4));
    }

    @Test
    public void testInvalidUser() {
        User user = new User();
        user.setEmail("wrongEmail");
        user.setLastName("Turing");
        user.setFirstName("Alan");
        user.setPassword("tutu");
        user.setAdmin(false);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).hasSize(1);
    }

    @DirtiesContext
    public void testValidSession() {
        User user = CreateAndSaveUser("1");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).isEmpty();
    }

    @Test
    @DirtiesContext
    public void testFindAll() {
        User user = CreateAndSaveUser("1");
        List<User> users = userRepository.findAll();
        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
    }

    @Test
    @DirtiesContext
    public void testFindById() {
        User user = CreateAndSaveUser("1");
        User foundTeacher = userRepository.findById(user.getId()).orElse(null);
        assertNotNull(foundTeacher);
        assertEquals(user, foundTeacher);
    }

    @Test
    public void testDelete() {
        User user = CreateAndSaveUser("1");
        userRepository.delete(user);
        assertFalse(userRepository.existsById(user.getId()));
    }
    @Test
    public void TestBuilder(){

        LocalDateTime timeNow =LocalDateTime.now();

        String user1 = User.builder()
                .id(Long.parseLong("1"))
                .email("email1@gmail.com")
                .lastName("teach1")
                .firstName("prenom1")
                .createdAt(timeNow)
                .updatedAt(timeNow)
                .password("pwd")
                .admin(true)
                .toString();

        String user2 = User.builder()
                .id(Long.parseLong("1"))
                .email("email1@gmail.com")
                .lastName("teach1")
                .firstName("prenom1")
                .createdAt(timeNow)
                .updatedAt(timeNow)
                .password("pwd")
                .admin(true)
                .toString();

        assertTrue(user1.equals(user2));
    }
}
