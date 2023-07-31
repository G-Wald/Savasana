package com.openclassrooms.starterjwt.models;

import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
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
class TeacherTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    private TeacherRepository teacherRepository;

    public Teacher CreateAndSaveTeacher(String id){
        Teacher teacher =  Teacher.builder()
                .id(Long.parseLong(id))
                .lastName("teach")
                .firstName("prenom")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return teacherRepository.save(teacher);
    };

    @Test
    @DirtiesContext
    public void testValidTeacher() {
        Teacher teacher = new Teacher()
                .setFirstName("Alan")
                .setLastName("Turing");

        Teacher teacher2 = Teacher.builder()
                .id(Long.parseLong("2"))
                .lastName("teach2")
                .firstName("prenom2")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Teacher teacher3 = new Teacher(Long.parseLong("3"), "teach3", "prenom3", LocalDateTime.now(), LocalDateTime.now());

        teacher = teacherRepository.save(teacher);
        teacher2 = teacherRepository.save(teacher2);
        teacher3 = teacherRepository.save(teacher3);

        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        assertThat(violations).isEmpty();
        assertNotNull(teacher.getId());
        assertEquals("Alan", teacher.getFirstName());
        assertEquals("Turing", teacher.getLastName());
        assertNotNull(teacher.getCreatedAt());

        assertNotNull(teacher2.getId());
        assertEquals(Long.parseLong("2"), teacher2.getId());
        assertEquals("prenom2", teacher2.getFirstName());
        assertEquals("teach2", teacher2.getLastName());
        assertEquals(LocalDateTime.now().toString().substring(0,10), teacher2.getCreatedAt().toString().substring(0,10));
        assertEquals(LocalDateTime.now().toString().substring(0,10), teacher2.getUpdatedAt().toString().substring(0,10));

        assertNotNull(teacher3.getId());
        assertEquals("prenom3", teacher3.getFirstName());
        assertEquals("teach3", teacher3.getLastName());
        assertNotNull(teacher3.getCreatedAt());

        teacher.setFirstName("Jane");
        teacher.setUpdatedAt(LocalDateTime.now());
        teacher = teacherRepository.save(teacher);

        assertEquals("Jane", teacher.getFirstName());
        assertNotNull(teacher.getUpdatedAt());
        assertEquals(teacher.toString().substring(0, 56), "Teacher(id=1, lastName=Turing, firstName=Jane, createdAt");
        Teacher newTeacher = new Teacher(teacher.getId(), teacher.getLastName(), teacher.getFirstName(), teacher.getCreatedAt(), teacher.getUpdatedAt());
        assertEquals(teacher.hashCode(), newTeacher.hashCode());
        assertTrue(teacher.equals(newTeacher));
    }

    @Test
    @DirtiesContext
    public void testInvalidTeacher() {
        Teacher teacher = new Teacher();
        teacher.setCreatedAt(LocalDateTime.now());
        teacher.setUpdatedAt(LocalDateTime.now());

        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        assertThat(violations).hasSize(2);
    }

    @Test
    public void testCreateInvalidTeacher() {
        // Tentative de créer un enseignant avec des champs vides
        Teacher invalidTeacher = new Teacher();

        // Vérifier qu'une exception est levée lors de l'enregistrement de l'enseignant
        assertThrows(ConstraintViolationException.class, () -> teacherRepository.save(invalidTeacher));
    }
    @Test
    @DirtiesContext
    public void testValidSession() {
        Teacher teacher = CreateAndSaveTeacher("1");
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        assertThat(violations).isEmpty();
    }

    @Test
    @DirtiesContext
    public void testFindAll() {
        Teacher teacher = CreateAndSaveTeacher("1");
        List<Teacher> teachers = teacherRepository.findAll();
        assertEquals(1, teachers.size());
        assertEquals(teacher, teachers.get(0));
    }

    @Test
    @DirtiesContext
    public void testFindById() {
        Teacher teacher = CreateAndSaveTeacher("1");
        Teacher foundTeacher = teacherRepository.findById(teacher.getId()).orElse(null);
        assertNotNull(foundTeacher);
        assertEquals(teacher, foundTeacher);
    }

    @Test
    public void testDelete() {
        Teacher teacher = CreateAndSaveTeacher("1");
        teacherRepository.delete(teacher);
        assertFalse(teacherRepository.existsById(teacher.getId()));
    }
    @Test
    public void TestBuilder(){

        LocalDateTime timeNow =LocalDateTime.now();

        String teacher1 = Teacher.builder()
                .id(Long.parseLong("2"))
                .lastName("teach2")
                .firstName("prenom2")
                .createdAt(timeNow)
                .updatedAt(timeNow)
                .toString();

        String teacher2 = Teacher.builder()
                .id(Long.parseLong("2"))
                .lastName("teach2")
                .firstName("prenom2")
                .createdAt(timeNow)
                .updatedAt(timeNow)
                .toString();

        assertTrue(teacher1.equals(teacher2));
    }
}