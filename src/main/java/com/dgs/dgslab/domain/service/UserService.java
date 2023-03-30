package com.dgs.dgslab.domain.service;

import com.dgs.dgslab.domain.exceptions.UserNotFoundException;
import com.dgs.dgslab.domain.model.User;
import com.dgs.dgslab.domain.repository.UserRepository;
import com.dgs.dgslab.infraestructure.aop.Log;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Log
    public List<User> getAllUsers() {
         return repository.findAll();
    }

    @Log
    public User saveUser(User user) {
        return repository.save(user);
    }

    @Log
    public User findById(Long id) {
        return repository.findById(id).orElseThrow(() ->new UserNotFoundException(id));
    }

    @Log
    public void removeUser(Long userId) {
        this.findById(userId);
        repository.deleteById(userId);
    }
}
