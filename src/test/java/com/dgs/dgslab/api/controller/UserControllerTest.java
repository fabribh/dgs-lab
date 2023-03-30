package com.dgs.dgslab.api.controller;

import com.dgs.dgslab.domain.exceptions.UserNotFoundException;
import com.dgs.dgslab.domain.model.User;
import com.dgs.dgslab.domain.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    public static final String URI = "/api/v1/user";
    public static final SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor AUTH =
            SecurityMockMvcRequestPostProcessors.user("dgs")
                    .password("dgs123")
                    .roles("ADMIN");

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @MockBean
    private BeanUtils beanUtils;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testFindAllUsers() throws Exception {
        List<User> users = Arrays.asList(getUser(2L), getUser(3L), getUser(4L));

        when(userService.getAllUsers())
                .thenReturn(users);

        mockMvc
                .perform(get(URI)
                        .with(AUTH)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(3)))
                .andExpect(jsonPath("$[0].id").value(2L));
    }

    @Test
    public void testFindUserById() throws Exception {
        Long userId = 1L;

        User user = getUser(userId);

        when(userService.findById(userId))
                .thenReturn(user);

        mockMvc
                .perform(get(URI.concat("/{userId}"), userId)
                        .with(AUTH)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

    }

    @Test
    public void testAddUser() throws Exception {
        User user = new User();
        user.setName("User");
        user.setEmail("email@email.com");
        user.setPassword("123");

        user.setId(10L);

        when(userService.saveUser(user))
                .thenReturn(user);

        mockMvc
                .perform(post(URI)
                        .with(AUTH)
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    public void testDeleteUser() throws Exception {
        Long userId = 1L;

        mockMvc
                .perform(delete(URI.concat("/{userId}"), userId)
                        .with(AUTH))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteUserWithNotFoundId() {
        Long userId = 100L;
        doThrow(new UserNotFoundException(userId)).when(userService).removeUser(userId);
        assertThrows(UserNotFoundException.class, () -> userService.removeUser(userId));
    }

    @Test
    public void testFindUserByIdWithNotFoundId() {
        Long userId = 50L;
        doThrow(new UserNotFoundException(userId)).when(userService).findById(userId);
        assertThrows(UserNotFoundException.class, () -> userService.findById(userId));
    }

    @Test
    public void testUpdateUser() throws Exception {
        Long userId = 1L;
        User user = new User("New User", "new.user@email.com", "abcdfg");
        user.setId(userId);

        User existingUser = getUser(userId);
        existingUser.setName("New User");
        existingUser.setEmail("new.user@email.com");
        existingUser.setPassword("abcdfg");

        when(userService.findById(userId)).thenReturn(existingUser);

        mockMvc
                .perform(put(URI.concat("/{userId}"), userId)
                        .with(AUTH)
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private static User getUser(Long userId) {
        var user = new User();
        user.setId(userId);
        user.setName("User");
        user.setEmail("email@email.com");
        user.setPassword("123");
        return user;
    }
}