
package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AuthEntryPointJwtTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCommence() throws Exception {
        mockMvc.perform(get("/api/user/1")) // Simule une requête GET sur l'endpoint protégé
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Full authentication is required to access this resource"))
                .andExpect(jsonPath("$.path").value(""));
    }

    @Test
    @WithMockUser
    @DirtiesContext// Pour simuler un utilisateur authentifié
    public void testCommence_AuthenticatedUser() throws Exception {

        User user = new User();
        user.setFirstName("alan");
        user.setLastName("turing");
        user.setEmail("alan.turing@gmail.com");
        user.setPassword("tutu");
        user.setAdmin(true);
        userRepository.save(user);

        mockMvc.perform(get("/api/user/1")) // Simule une requête GET sur l'endpoint protégé
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("alan"))
                .andExpect(jsonPath("$.lastName").value("turing"))
                .andExpect(jsonPath("$.email").value("alan.turing@gmail.com"));
    }

}