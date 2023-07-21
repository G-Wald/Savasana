package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class TeacherTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void testValidTeacher() {
        Teacher teacher = new Teacher();
        teacher.setLastName("Turing");
        teacher.setFirstName("Alan");
        teacher.setCreatedAt(LocalDateTime.now());
        teacher.setUpdatedAt(LocalDateTime.now());

        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        assertThat(violations).isEmpty();
    }

    @Test
    public void testInvalidTeacher() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("Alan");
        teacher.setCreatedAt(LocalDateTime.now());
        teacher.setUpdatedAt(LocalDateTime.now());

        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        assertThat(violations).hasSize(1);
    }
}