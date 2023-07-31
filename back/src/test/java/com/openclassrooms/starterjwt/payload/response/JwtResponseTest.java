package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
class JwtResponseTest {

    @Test
    public void testJwtResponseGettersAndSetters() {
        String accessToken = "testAccessToken";
        Long id = 1L;
        String username = "testUser";
        String firstName = "Alan";
        String lastName = "Coben";
        Boolean admin = true;

        JwtResponse jwtResponse = new JwtResponse(accessToken, id, username, firstName, lastName, admin);

        assertEquals(accessToken, jwtResponse.getToken());
        assertEquals("Bearer", jwtResponse.getType());
        assertEquals(id, jwtResponse.getId());
        assertEquals(username, jwtResponse.getUsername());
        assertEquals(firstName, jwtResponse.getFirstName());
        assertEquals(lastName, jwtResponse.getLastName());
        assertEquals(admin, jwtResponse.getAdmin());

        String newAccessToken = "newAccessToken";
        jwtResponse.setToken(newAccessToken);
        jwtResponse.setType("NewType");
        Long newId = 2L;
        jwtResponse.setId(newId);
        String newUsername = "newUser";
        jwtResponse.setUsername(newUsername);
        String newFirstName = "Jack";
        jwtResponse.setFirstName(newFirstName);
        String newLastName = "Sparrow";
        jwtResponse.setLastName(newLastName);
        Boolean newAdmin = false;
        jwtResponse.setAdmin(newAdmin);

        assertEquals(newAccessToken, jwtResponse.getToken());
        assertEquals("NewType", jwtResponse.getType());
        assertEquals(newId, jwtResponse.getId());
        assertEquals(newUsername, jwtResponse.getUsername());
        assertEquals(newFirstName, jwtResponse.getFirstName());
        assertEquals(newLastName, jwtResponse.getLastName());
        assertEquals(newAdmin, jwtResponse.getAdmin());
    }
}