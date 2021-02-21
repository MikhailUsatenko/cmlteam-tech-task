package com.example.demo.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientResponseException;

public class NotFoundException extends RestClientResponseException {

    public NotFoundException(String errorMessage) {
        super(errorMessage, HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), null, null, null);
        this.errorMessage = errorMessage;
    }

    @JsonProperty
    private boolean success;
    @JsonProperty("error")
    private String errorMessage;
}
