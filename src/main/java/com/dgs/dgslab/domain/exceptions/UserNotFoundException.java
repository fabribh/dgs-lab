package com.dgs.dgslab.domain.exceptions;

public class UserNotFoundException extends BusinessException{
    public UserNotFoundException(String message) {
        super(message);
    }
    public UserNotFoundException(Long userId) {
        this(String.format("Not found a user with id %d", userId));
    }
}
