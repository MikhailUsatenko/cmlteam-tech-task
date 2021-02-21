package com.example.demo.factory.exception;

import com.example.demo.exception.NotFoundException;

public class NotFoundExceptionFactory {
    public static NotFoundException create(String errorMessage) {
        var exception = new NotFoundException(errorMessage);
        return exception;
    }
}
