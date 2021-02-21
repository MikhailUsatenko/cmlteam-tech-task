package com.example.demo.controller.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FileUploadResponseBean {

    @JsonProperty("ID")
    private String id;
}
