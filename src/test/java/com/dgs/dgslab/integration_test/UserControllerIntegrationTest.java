package com.dgs.dgslab.integration_test;

import com.dgs.dgslab.domain.model.User;
import com.dgs.dgslab.domain.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerIntegrationTest {

    public static final String URI = "/api/v1/user";

    public static final SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor AUTH =
            SecurityMockMvcRequestPostProcessors.user("dgs")
                    .password("dgs123")
                    .roles("ADMIN");
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserRepository repository;

    @BeforeEach
    public void setupDataBase() {
        repository.deleteAll();
        User pedro = new User("Pedro", "pedro@email.com", "123456");
        User maria = new User("Maria", "maria@email.com", "abcefg");
        repository.saveAll(Arrays.asList(pedro, maria));
    }

    @Test
    public void testFindAllUsers() throws Exception {
        mockMvc
                .perform(get(URI)
                        .with(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].name", is("Pedro")))
                .andExpect(jsonPath("$[1].name", is("Maria")));
    }

    @Test
    public void testFindUserById() throws Exception {
        var user = repository.findAll().get(0);
        var userId = user.getId();

        mockMvc
                .perform(get(URI.concat("/{userId}"), userId)
                        .with(AUTH))
                .andExpect(jsonPath("$.id", is(userId.intValue())))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    public void testFindUserByIdUserNotFound() throws Exception {
        mockMvc
                .perform(get(URI.concat("/{userId}"), 100L)
                        .with(AUTH))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddUser() throws Exception {
        User user = new User("New User", "new.user@email.com", "123456");

        mockMvc
                .perform(post(URI)
                        .with(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    public void testDeleteUser() throws Exception {
        User user = repository.findAll().get(0);
        Long userId = user.getId();

        mockMvc
                .perform(delete(URI.concat("/{userId}"), userId)
                        .with(AUTH))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteUserNotFound() throws Exception {
        mockMvc
                .perform(delete(URI.concat("/{userId}"), 100L)
                        .with(AUTH))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateUser() throws Exception {
        User user = repository.findAll().get(0);
        Long userId = user.getId();
        String newName = "Updated Name";
        String newEmail = "updated.email@email.com";
        String newPass = "abcdefgh";

        user.setName(newName);
        user.setEmail(newEmail);
        user.setPassword(newPass);

        mockMvc
                .perform(put(URI.concat("/{userId}"), userId)
                        .with(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userId.intValue())))
                .andExpect(jsonPath("$.name", is(newName)))
                .andExpect(jsonPath("$.email", is(newEmail)));
    }

    @Test
    public void testUpdateUserNotFound() throws Exception {
        mockMvc
                .perform(put(URI.concat("/{userId}"), 100L)
                        .with(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new User())))
                .andExpect(status().isNotFound());
    }
}
