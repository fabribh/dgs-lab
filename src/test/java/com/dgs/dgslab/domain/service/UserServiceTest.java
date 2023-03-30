package com.dgs.dgslab.domain.service;

import com.dgs.dgslab.domain.model.User;
import com.dgs.dgslab.domain.exceptions.UserNotFoundException;
import com.dgs.dgslab.domain.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyLong;

class UserServiceTest {
    @Mock
    UserRepository repository;

    private UserService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new UserService(repository);
    }

    @Test
    void testGetAllUsers() {
        List<User> userList = new ArrayList();
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        userList.add(user);

        when(repository.findAll()).thenReturn(userList);

        List<User> result = service.getAllUsers();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(user.getName(), result.get(0).getName());
    }

    @Test
    void testCreateUser() {
        User user = new User();
        user.setName("Jane Smith");

        when(repository.save(any(User.class))).thenReturn(user);

        User result = service.saveUser(user);

        Assertions.assertEquals(user.getName(), result.getName());
    }

    @Test
    void testFindById_Success() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");

        when(repository.findById(anyLong())).thenReturn(java.util.Optional.of(user));

        User result = service.findById(1L);

        Assertions.assertEquals(user.getName(), result.getName());
    }

    @Test
    void testFindById_Failure() {
        when(repository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            service.findById(1L);
        });
    }

    @Test
    void testRemoveUser_Success() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");

        when(repository.findById(anyLong())).thenReturn(java.util.Optional.of(user));

        service.removeUser(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testRemoveUser_Failure() {
        when(repository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            service.removeUser(1L);
        });
    }
}