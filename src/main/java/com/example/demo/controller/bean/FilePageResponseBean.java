package com.example.demo.controller.bean;

import com.example.demo.pojo.File;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class FilePageResponseBean {

    @JsonProperty
    private long total;

    @JsonProperty("page")
    private List<File> files;
}
