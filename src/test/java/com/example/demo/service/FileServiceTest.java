package com.example.demo.service;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.message.ExceptionMessages;
import com.example.demo.factory.bean.FilePageResponseBeanFactory;
import com.example.demo.factory.bean.FileUploadRequestBeanFactory;
import com.example.demo.factory.bean.FileUploadResponseBeanFactory;
import com.example.demo.factory.bean.SuccessResponseBeanFactory;
import com.example.demo.factory.pojo.FileFactory;
import com.example.demo.pojo.File;
import com.example.demo.repository.FileRepository;
import com.example.demo.utils.ElasticsearchQueryUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { FileService.class })
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class FileServiceTest {

    private final FileService fileService;
    @MockBean
    private FileRepository fileRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void upload() {
        log.info("Test class: {}; Test method: {}; Description: {}", FileService.class.getSimpleName(), "upload", "Method description");
        // arrange
        var name = "file.txt";
        var size = 1000L;
        var requestBean = FileUploadRequestBeanFactory.create(name, size);
        var file = FileFactory.create(requestBean);
        var fileWithId = FileFactory.create("abc", name, size);
        var expectedResponseBean = FileUploadResponseBeanFactory.create(fileWithId);
        when(fileRepository.save(file)).thenReturn(fileWithId);
        // act
        var responseBean = fileService.upload(requestBean);
        // assert
        assertEquals(expectedResponseBean, responseBean);
        verify(fileRepository, times(1)).save(file);
    }

    @Test
    void delete() {
        log.info("Test class: {}; Test method: {}; Description: {}", FileService.class.getSimpleName(), "delete", "Method description");
        // arrange
        var id = "abc";
        var file = FileFactory.create("abc", "file.txt", 1000L);
        var mockFileOptional = Optional.of(file);
        var expectedResponseBean = SuccessResponseBeanFactory.create();
        when(fileRepository.findById(id)).thenReturn(mockFileOptional);
        // act
        var responseBean = fileService.delete(id);
        // assert
        assertEquals(expectedResponseBean, responseBean);
        verify(fileRepository, times(1)).findById(id);
        verify(fileRepository, times(1)).delete(file);
    }

    @Test
    void fileNotFoundForDelete() {
        log.info("Test class: {}; Test method: {}; Description: {}", FileService.class.getSimpleName(), "delete", "Method description");
        // arrange
        var id = "abc";
        var file = FileFactory.create("abc", "file.txt", 1000L);
        var mockFileOptional = Optional.ofNullable((File) null);
        when(fileRepository.findById(id)).thenReturn(mockFileOptional);
        // act
        var executable = (Executable) () -> fileService.delete(id);
        // assert
        assertThrows(NotFoundException.class, executable, ExceptionMessages.FILE_NOT_FOUND);
        verify(fileRepository, times(1)).findById(id);
        verify(fileRepository, times(0)).delete(file);
    }

    @Test
    void assignTags() {
        log.info("Test class: {}; Test method: {}; Description: {}", FileService.class.getSimpleName(), "assignTags", "Method description");
        // arrange
        var id = "abc";
        var tags = List.of("tag1", "tag2", "tag3");
        var file = FileFactory.create("abc", "file.txt", 1000L);
        var mockFileOptional = Optional.of(file);
        var expectedResponseBean = SuccessResponseBeanFactory.create();
        when(fileRepository.findById(id)).thenReturn(mockFileOptional);
        // act
        var responseBean = fileService.assignTags(id, tags);
        // assert
        assertEquals(expectedResponseBean, responseBean);
        assertEquals(tags, file.getTags());
        verify(fileRepository, times(1)).findById(id);
        verify(fileRepository, times(1)).save(file);
    }

    @Test
    void fileNotFoundForAssignTags() {
        log.info("Test class: {}; Test method: {}; Description: {}", FileService.class.getSimpleName(), "assignTags", "Method description");
        // arrange
        var id = "abc";
        var tags = List.of("tag1", "tag2", "tag3");
        var file = FileFactory.create("abc", "file.txt", 1000L);
        var mockFileOptional = Optional.ofNullable((File) null);
        when(fileRepository.findById(id)).thenReturn(mockFileOptional);
        // act
        var executable = (Executable) () -> fileService.assignTags(id, tags);
        // assert
        assertThrows(NotFoundException.class, executable, ExceptionMessages.FILE_NOT_FOUND);
        assertTrue(file.getTags().isEmpty());
        verify(fileRepository, times(1)).findById(id);
        verify(fileRepository, times(0)).save(file);
    }

    @Test
    void deleteTags() {
        log.info("Test class: {}; Test method: {}; Description: {}", FileService.class.getSimpleName(), "deleteTags", "Method description");
        // arrange
        var id = "abc";
        var tags = new ArrayList<>(List.of("tag1", "tag2", "tag3"));
        var tagsQueryExpr = ElasticsearchQueryUtils.getTagsQueryExpr(tags);
        var file = FileFactory.create("abc", "file.txt", 1000L, tags);
        var mockFileOptional = Optional.of(file);
        var expectedResponseBean = SuccessResponseBeanFactory.create();
        when(fileRepository.findByIdAndTagsUsingCustomQuery(id, tagsQueryExpr)).thenReturn(mockFileOptional);
        // act
        var responseBean = fileService.deleteTags(id, tags);
        // assert
        assertEquals(expectedResponseBean, responseBean);
        assertTrue(file.getTags().isEmpty());
        verify(fileRepository, times(1)).findByIdAndTagsUsingCustomQuery(id, tagsQueryExpr);
        verify(fileRepository, times(1)).save(file);
    }

    @Test
    void tagNotFound() {
        log.info("Test class: {}; Test method: {}; Description: {}", FileService.class.getSimpleName(), "deleteTags", "Method description");
        // arrange
        var id = "abc";
        var tags = List.of("tag1", "tag2", "tag3");
        var tagsQueryExpr = ElasticsearchQueryUtils.getTagsQueryExpr(tags);
        var file = FileFactory.create("abc", "file.txt", 1000L);
        var mockFileOptional = Optional.ofNullable((File) null);
        when(fileRepository.findByIdAndTagsUsingCustomQuery(id, tagsQueryExpr)).thenReturn(mockFileOptional);
        // act
        var executable = (Executable) () -> fileService.deleteTags(id, tags);
        // assert
        assertThrows(BadRequestException.class, executable, ExceptionMessages.TAG_NOT_FOUND);
        assertTrue(file.getTags().isEmpty());
        verify(fileRepository, times(1)).findByIdAndTagsUsingCustomQuery(id, tagsQueryExpr);
        verify(fileRepository, times(0)).save(file);

    }

    @Test
    void getFiles() {
        log.info("Test class: {}; Test method: {}; Description: {}", FileService.class.getSimpleName(), "getFiles", "Method description");
        // arrange
        var page = 1;
        var size = 5;
        var tags = List.of("tag1", "tag2");
        var tagsQueryExpr = ElasticsearchQueryUtils.getTagsQueryExpr(tags);
        var pageRequest = PageRequest.of(page, size);
        var files = List.of(FileFactory.create("abc", "file.txt", 1000L, tags));
        var mockFilesPage = (Page<File>) new PageImpl(files);
        var expectedResponseBean = FilePageResponseBeanFactory.create(mockFilesPage);
        when(fileRepository.findAllByTagsUsingCustomQuery(tagsQueryExpr, pageRequest)).thenReturn(mockFilesPage);
        // act
        var responseBean = fileService.getFiles(page, size, tags);
        // assert
        assertEquals(expectedResponseBean, responseBean);
        verify(fileRepository, times(1)).findAllByTagsUsingCustomQuery(tagsQueryExpr, pageRequest);
    }
}