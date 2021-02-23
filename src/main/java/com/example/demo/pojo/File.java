package com.example.demo.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.HashSet;
import java.util.Set;

@Document(indexName = "file")
@Data
public class File {

    @Id
    @JsonProperty
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty
    private long size;

    @JsonProperty
    private Set<String> tags = new HashSet<>();
}
