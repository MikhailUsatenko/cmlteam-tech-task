package com.example.demo.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientResponseException;

public class BadRequestException extends RestClientResponseException {

    public BadRequestException(String errorMessage) {
        super(errorMessage, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        this.errorMessage = errorMessage;
    }

    @JsonProperty
    private boolean success;
    @JsonProperty("error")
    private String errorMessage;
}
