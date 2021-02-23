package com.example.demo.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class JacksonUtils {

    private static ObjectMapper mapper = getObjectMapper();

    public static ObjectMapper getObjectMapper() {
        return Optional.ofNullable(mapper).orElseGet(JacksonUtils::createMapper);
    }

    public static String getJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Cannot get json from the object {}", object);
        }
        return null;
    }

    private static ObjectMapper createMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        objectMapper.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.DEFAULT);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.DEFAULT);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }

}
