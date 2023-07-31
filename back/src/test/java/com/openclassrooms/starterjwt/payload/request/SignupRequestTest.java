package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SignupRequestTest {
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @Test
    public void testValidSignupRequest() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.isEmpty());
    }
    @Test
    public void testEqualSignupRequest() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.isEmpty());

        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        assertEquals(signupRequest.toString(), "SignupRequest(email=test@example.com, firstName=John, lastName=Doe, password=password123)");
        assertEquals(signupRequest.hashCode(), signupRequest.hashCode());
        assertNotEquals(signupRequest2.hashCode(), signupRequest.hashCode());

        SignupRequest signupRequest3 = new SignupRequest();
        signupRequest.setEmail(signupRequest2.getEmail());
        signupRequest.setFirstName(signupRequest2.getFirstName());
        signupRequest.setLastName(signupRequest2.getLastName());
        signupRequest.setPassword(signupRequest2.getPassword());

        assertTrue(signupRequest2.equals(signupRequest3));
        assertTrue(signupRequest2.canEqual(signupRequest3));

    }
    @Test
    public void testInvalidSignupRequest() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("invalid-email");
        signupRequest.setFirstName("");
        signupRequest.setLastName("L");
        signupRequest.setPassword("short");
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertEquals(5, violations.size());
    }
}