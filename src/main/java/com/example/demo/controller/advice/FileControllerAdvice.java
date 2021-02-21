package com.example.demo.controller.advice;

import com.example.demo.controller.FileController;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.factory.exception.BadRequestExceptionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice(basePackageClasses = {FileController.class})
@Slf4j
public class FileControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestException handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("Handle the MethodArgumentNotValidException with message: {}", exception.getMessage());
        var errorMessage = getErrorMessage(exception);
        var badRequestException = BadRequestExceptionFactory.create(errorMessage);
        return badRequestException;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestException handleUnauthorizedException(BadRequestException badRequestException) {
        log.error("Handle the BadRequestException with message: {}", badRequestException.getMessage());
        return badRequestException;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public NotFoundException handleNotFoundException(NotFoundException notFoundException) {
        log.error("Handle the NotFoundException with message: {}", notFoundException.getMessage());
        return notFoundException;
    }

    private String getErrorMessage(MethodArgumentNotValidException exception) {
        return exception.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));
    }
}
