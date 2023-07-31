package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @Test
    public void testValidLoginRequest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidLoginRequest() {
        LoginRequest loginRequest = new LoginRequest();
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        assertEquals(2, violations.size());
    }
}