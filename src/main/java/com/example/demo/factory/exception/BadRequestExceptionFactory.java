package com.example.demo.factory.exception;

import com.example.demo.exception.BadRequestException;

public class BadRequestExceptionFactory {
    public static BadRequestException create(String errorMessage) {
        var exception = new BadRequestException(errorMessage);
        return exception;
    }
}
