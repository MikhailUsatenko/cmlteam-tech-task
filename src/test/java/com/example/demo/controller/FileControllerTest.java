package com.example.demo.controller;

import com.example.demo.config.AppConfig;
import com.example.demo.controller.advice.FileControllerAdvice;
import com.example.demo.controller.api.ControllerAPI;
import com.example.demo.exception.message.ExceptionMessages;
import com.example.demo.factory.bean.FilePageResponseBeanFactory;
import com.example.demo.factory.bean.FileUploadRequestBeanFactory;
import com.example.demo.factory.bean.FileUploadResponseBeanFactory;
import com.example.demo.factory.bean.SuccessResponseBeanFactory;
import com.example.demo.factory.exception.BadRequestExceptionFactory;
import com.example.demo.factory.exception.NotFoundExceptionFactory;
import com.example.demo.factory.pojo.FileFactory;
import com.example.demo.service.FileService;
import com.example.demo.utils.JacksonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = { AppConfig.class, FileController.class, FileControllerAdvice.class })
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class FileControllerTest {

    private final MockMvc mvc;
    @MockBean
    private FileService fileService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }


    /**
     * Curl Script: curl -i -X POST "http://localhost:8080/file" -H "Content-Type: application/json" -d '{"name":"file.txt","size":1000}'
     */
    @Test
    void upload() throws Exception {
        log.info("Test class: {}; Test method: {}; Description: {}", FileController.class.getSimpleName(), "upload", "Method description");
        // arrange
        var url = ControllerAPI.FILE_CONTROLLER + ControllerAPI.FILE_CONTROLLER_POST_UPLOAD_FILE;
        var requestBody = FileUploadRequestBeanFactory.create("file.txt", 1000L);
        var requestBodyJson = JacksonUtils.getJson(requestBody);
        var expectedResponseBody = FileUploadResponseBeanFactory.create("");
        var expectedResponseBodyJson = JacksonUtils.getJson(expectedResponseBody);
        var request = MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(requestBodyJson);
        when(fileService.upload(requestBody)).thenReturn(expectedResponseBody);
        // act
        var resultActions = mvc.perform(request);
        // assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().string(expectedResponseBodyJson));
        verify(fileService, times(1)).upload(requestBody);
    }

    /**
     * Curl Script: curl -i -X POST "http://localhost:8080/file" -H "Content-Type: application/json" -d '{"size":1000}'
     */
    @Test
    void nameIsNotPresent() throws Exception {
        log.info("Test class: {}; Test method: {}; Description: {}", FileController.class.getSimpleName(), "upload", "Method description");
        // arrange
        var url = ControllerAPI.FILE_CONTROLLER + ControllerAPI.FILE_CONTROLLER_POST_UPLOAD_FILE;
        var requestBody = FileUploadRequestBeanFactory.create(null, 1000L);
        var requestBodyJson = JacksonUtils.getJson(requestBody);
        var expectedResponseBody = BadRequestExceptionFactory.create(ExceptionMessages.FILENAME_MUST_NOT_BE_BLANK);
        var expectedResponseBodyJson = JacksonUtils.getJson(expectedResponseBody);
        var request = MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(requestBodyJson);
        // act
        var resultActions = mvc.perform(request);
        // assert
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest()).andExpect(MockMvcResultMatchers.content().string(expectedResponseBodyJson));
    }

    /**
     * Curl Script: curl -i -X POST "http://localhost:8080/file" -H "Content-Type: application/json" -d '{"name":"   ","size":1000}'
     */
    @Test
    void nameIsBlank() throws Exception {
        log.info("Test class: {}; Test method: {}; Description: {}", FileController.class.getSimpleName(), "upload", "Method description");
        // arrange
        var url = ControllerAPI.FILE_CONTROLLER + ControllerAPI.FILE_CONTROLLER_POST_UPLOAD_FILE;
        var requestBody = FileUploadRequestBeanFactory.create("   ", 1000L);
        var requestBodyJson = JacksonUtils.getJson(requestBody);
        var expectedResponseBody = BadRequestExceptionFactory.create(ExceptionMessages.FILENAME_MUST_NOT_BE_BLANK);
        var expectedResponseBodyJson = JacksonUtils.getJson(expectedResponseBody);
        var request = MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(requestBodyJson);
        // act
        var resultActions = mvc.perform(request);
        // assert
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest()).andExpect(MockMvcResultMatchers.content().string(expectedResponseBodyJson));
    }

    /**
     * Curl Script: curl -i -X POST "http://localhost:8080/file" -H "Content-Type: application/json" -d '{"name":"file.txt"}'
     */
    @Test
    void sizeIsNotPresent() throws Exception {
        log.info("Test class: {}; Test method: {}; Description: {}", FileController.class.getSimpleName(), "upload", "Method description");
        // arrange
        var url = ControllerAPI.FILE_CONTROLLER + ControllerAPI.FILE_CONTROLLER_POST_UPLOAD_FILE;
        var requestBody = FileUploadRequestBeanFactory.create("file.txt", null);
        var requestBodyJson = JacksonUtils.getJson(requestBody);
        var expectedResponseBody = BadRequestExceptionFactory.create(ExceptionMessages.FILE_SIZE_MUST_NOT_BE_NULL);
        var expectedResponseBodyJson = JacksonUtils.getJson(expectedResponseBody);
        var request = MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(requestBodyJson);
        // act
        var resultActions = mvc.perform(request);
        // assert
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest()).andExpect(MockMvcResultMatchers.content().string(expectedResponseBodyJson));
    }

    /**
     * Curl Script: curl -i -X POST "http://localhost:8080/file" -H "Content-Type: application/json" -d '{"name":"file.txt","size":-1}'
     */
    @Test
    void sizeIsNegative() throws Exception {
        log.info("Test class: {}; Test method: {}; Description: {}", FileController.class.getSimpleName(), "upload", "Method description");
        // arrange
        var url = ControllerAPI.FILE_CONTROLLER + ControllerAPI.FILE_CONTROLLER_POST_UPLOAD_FILE;
        var requestBody = FileUploadRequestBeanFactory.create("file.txt", -1L);
        var requestBodyJson = JacksonUtils.getJson(requestBody);
        var expectedResponseBody = BadRequestExceptionFactory.create(ExceptionMessages.FILE_SIZE_MUST_NOT_BE_NEGATIVE);
        var expectedResponseBodyJson = JacksonUtils.getJson(expectedResponseBody);
        var request = MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(requestBodyJson);
        // act
        var resultActions = mvc.perform(request);
        // assert
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest()).andExpect(MockMvcResultMatchers.content().string(expectedResponseBodyJson));
    }


    /**
     * Curl Script: curl -i -X DELETE "http://localhost:8080/file/abc" -H "Content-Type: application/json"
     */
    @Test
    void delete() throws Exception {
        log.info("Test class: {}; Test method: {}; Description: {}", FileController.class.getSimpleName(), "delete", "Method description");
        // arrange
        var url = ControllerAPI.FILE_CONTROLLER + ControllerAPI.FILE_CONTROLLER_DELETE_FILE;
        var id = "abc";
        var expectedResponseBody = SuccessResponseBeanFactory.create();
        var expectedResponseBodyJson = JacksonUtils.getJson(expectedResponseBody);
        var request = MockMvcRequestBuilders.delete(url, id).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        when(fileService.delete(id)).thenReturn(expectedResponseBody);
        // act
        var resultActions = mvc.perform(request);
        // assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().string(expectedResponseBodyJson));
        verify(fileService, times(1)).delete(id);
    }

    /**
     * Curl Script: curl -i -X DELETE "http://localhost:8080/file/abc" -H "Content-Type: application/json"
     */
    @Test
    void fileNotFound() throws Exception {
        log.info("Test class: {}; Test method: {}; Description: {}", FileController.class.getSimpleName(), "delete", "Method description");
        // arrange
        var url = ControllerAPI.FILE_CONTROLLER + ControllerAPI.FILE_CONTROLLER_DELETE_FILE;
        var id = "abc";
        var expectedResponseBody = NotFoundExceptionFactory.create(ExceptionMessages.FILE_NOT_FOUND);
        var expectedResponseBodyJson = JacksonUtils.getJson(expectedResponseBody);
        var request = MockMvcRequestBuilders.delete(url, id).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        when(fileService.delete(id)).thenThrow(expectedResponseBody);
        // act
        var resultActions = mvc.perform(request);
        // assert
        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound()).andExpect(MockMvcResultMatchers.content().string(expectedResponseBodyJson));
        verify(fileService, times(1)).delete(id);
    }


    /**
     * Curl Script: curl -i -X POST "http://localhost:8080/file/abc/tags" -H "Content-Type: application/json" -d '["tag1", "tag2", "tag3"]'
     */
    @Test
    void assignTags() throws Exception {
        log.info("Test class: {}; Test method: {}; Description: {}", FileController.class.getSimpleName(), "assignTags", "Method description");
        // arrange
        var url = ControllerAPI.FILE_CONTROLLER + ControllerAPI.FILE_CONTROLLER_POST_ASSIGN_TAGS;
        var id = "abc";
        var requestBody = List.of("tag1", "tag2", "tag3");
        var requestBodyJson = JacksonUtils.getJson(requestBody);
        var expectedResponseBody = SuccessResponseBeanFactory.create();
        var expectedResponseBodyJson = JacksonUtils.getJson(expectedResponseBody);
        var request = MockMvcRequestBuilders.post(url, id).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(requestBodyJson);
        when(fileService.assignTags(id, requestBody)).thenReturn(expectedResponseBody);
        // act
        var resultActions = mvc.perform(request);
        // assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().string(expectedResponseBodyJson));
        verify(fileService, times(1)).assignTags(id, requestBody);
    }

    /**
     * Curl Script: curl -i -X DELETE "http://localhost:8080/file/abc/tags" -H "Content-Type: application/json" -d '["tag1", "tag2", "tag3"]'
     */
    @Test
    void deleteTags() throws Exception {
        log.info("Test class: {}; Test method: {}; Description: {}", FileController.class.getSimpleName(), "deleteTags", "Method description");
        // arrange
        var url = ControllerAPI.FILE_CONTROLLER + ControllerAPI.FILE_CONTROLLER_DELETE_TAGS;
        var id = "abc";
        var requestBody = List.of("tag1", "tag2", "tag3");
        var requestBodyJson = JacksonUtils.getJson(requestBody);
        var expectedResponseBody = SuccessResponseBeanFactory.create();
        var expectedResponseBodyJson = JacksonUtils.getJson(expectedResponseBody);
        var request = MockMvcRequestBuilders.delete(url, id).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(requestBodyJson);
        when(fileService.deleteTags(id, requestBody)).thenReturn(expectedResponseBody);
        // act
        var resultActions = mvc.perform(request);
        // assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().string(expectedResponseBodyJson));
        verify(fileService, times(1)).deleteTags(id, requestBody);
    }

    /**
     * Curl Script: curl -i -X DELETE "http://localhost:8080/file/abc/tags" -H "Content-Type: application/json" -d '["tag1", "tag2", "tag3"]'
     */
    @Test
    void tagNotFound() throws Exception {
        log.info("Test class: {}; Test method: {}; Description: {}", FileController.class.getSimpleName(), "deleteTags", "Method description");
        // arrange
        var url = ControllerAPI.FILE_CONTROLLER + ControllerAPI.FILE_CONTROLLER_DELETE_TAGS;
        var id = "abc";
        var requestBody = List.of("tag1", "tag2", "tag3");
        var requestBodyJson = JacksonUtils.getJson(requestBody);
        var expectedResponseBody = BadRequestExceptionFactory.create(ExceptionMessages.TAG_NOT_FOUND);
        var expectedResponseBodyJson = JacksonUtils.getJson(expectedResponseBody);
        var request = MockMvcRequestBuilders.delete(url, id).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(requestBodyJson);
        when(fileService.deleteTags(id, requestBody)).thenThrow(expectedResponseBody);
        // act
        var resultActions = mvc.perform(request);
        // assert
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest()).andExpect(MockMvcResultMatchers.content().string(expectedResponseBodyJson));
        verify(fileService, times(1)).deleteTags(id, requestBody);
    }

    /**
     * Curl Script: curl -i -X GET "http://localhost:8080/file?page=2&size=3&tags=tag1,tag2,tag3&q=aaa"
     */
    @Test
    void getFiles() throws Exception {
        log.info("Test class: {}; Test method: {}; Description: {}", FileController.class.getSimpleName(), "getFiles", "Method description");
        // arrange
        var url = ControllerAPI.FILE_CONTROLLER + ControllerAPI.FILE_CONTROLLER_GET_FILES;
        var pageQueryParamValue = 1;
        var sizeQueryParamValue = 5;
        var tagsQueryParamValue = "tag1,tag2,tag3";
        var tagsRequestParamValue = List.of(tagsQueryParamValue.split(","));
        var queryParams = new HttpHeaders() {{
            set("page", Integer.toString(pageQueryParamValue));
            set("size", Integer.toString(sizeQueryParamValue));
            set("tags", tagsQueryParamValue);
        }};
        var tags = List.of("tag1", "tag2", "tag3");
        var files = List.of(FileFactory.create("abc", "file.txt", 1000L, tags));
        var expectedResponseBody = FilePageResponseBeanFactory.create(1, files);
        var expectedResponseBodyJson = JacksonUtils.getJson(expectedResponseBody);
        var request = MockMvcRequestBuilders.get(url).queryParams(queryParams).accept(MediaType.APPLICATION_JSON);
        when(fileService.getFiles(pageQueryParamValue, sizeQueryParamValue, tagsRequestParamValue)).thenReturn(expectedResponseBody);
        // act
        var resultActions = mvc.perform(request);
        // assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().string(expectedResponseBodyJson));
        verify(fileService, times(1)).getFiles(pageQueryParamValue, sizeQueryParamValue, tagsRequestParamValue);
    }
}