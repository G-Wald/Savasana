package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void testValidUser() {
        User user = new User();
        user.setEmail("toto@example.com");
        user.setLastName("Turing");
        user.setFirstName("Alan");
        user.setPassword("tutu");
        user.setAdmin(false);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).isEmpty();
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
}
