package com.dgs.dgslab.api.controller;

import com.dgs.dgslab.domain.model.User;
import com.dgs.dgslab.domain.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<User> findAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping("{userId}")
    public User findUserById(@PathVariable Long userId) {
        return service.findById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody @Validated User user) {
        return service.saveUser(user);
    }

    @DeleteMapping("{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        service.removeUser(userId);
    }

    @PutMapping("/{userId}")
    public User updateUser(@PathVariable Long userId,
                           @RequestBody @Validated User user) {
        User actualUser = service.findById(userId);

        BeanUtils.copyProperties(user, actualUser, "id");

        return service.saveUser(actualUser);
    }
}
