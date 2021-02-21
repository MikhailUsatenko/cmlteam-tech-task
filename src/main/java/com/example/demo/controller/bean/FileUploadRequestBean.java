package com.example.demo.controller.bean;

import com.example.demo.exception.message.ExceptionMessages;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class FileUploadRequestBean {

    @JsonProperty
    @NotBlank(message = ExceptionMessages.FILENAME_MUST_NOT_BE_BLANK)
    private String name;

    @JsonProperty
    @NotNull(message = ExceptionMessages.FILE_SIZE_MUST_NOT_BE_NULL)
    @Min(value = 0, message = ExceptionMessages.FILE_SIZE_MUST_NOT_BE_NEGATIVE)
    private Long size;
}
