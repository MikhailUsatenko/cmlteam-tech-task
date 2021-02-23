package com.example.demo.service;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.message.ExceptionMessages;
import com.example.demo.factory.bean.FilePageResponseBeanFactory;
import com.example.demo.factory.bean.FileUploadRequestBeanFactory;
import com.example.demo.factory.bean.FileUploadResponseBeanFactory;
import com.example.demo.factory.bean.SuccessResponseBeanFactory;
import com.example.demo.pojo.File;
import com.example.demo.repository.FileRepository;
import com.example.demo.test.factory.pojo.FileFactory;
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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        fileService.addTagByFileExtension(file);
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
        Optional<File> mockFileOptional = Optional.empty();
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
        var tags = List.of("tag1", "tag1", "tag2", "tag3");
        var file = FileFactory.create("abc", "file.txt", 1000L);
        var mockFileOptional = Optional.of(file);
        var expectedResponseBean = SuccessResponseBeanFactory.create();
        var expectedFileTags = new HashSet<>(tags);
        when(fileRepository.findById(id)).thenReturn(mockFileOptional);
        // act
        var responseBean = fileService.assignTags(id, tags);
        // assert
        assertEquals(expectedResponseBean, responseBean);
        assertEquals(expectedFileTags, file.getTags());
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
        Optional<File> mockFileOptional = Optional.empty();
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
        var tags = List.of("tag1", "tag2", "tag3");
        var file = FileFactory.create("abc", "file.txt", 1000L, new HashSet<>(tags));
        var mockFileOptional = Optional.of(file);
        var expectedResponseBean = SuccessResponseBeanFactory.create();
        when(fileRepository.findByIdAndTags(id, tags)).thenReturn(mockFileOptional);
        // act
        var responseBean = fileService.deleteTags(id, tags);
        // assert
        assertEquals(expectedResponseBean, responseBean);
        assertTrue(file.getTags().isEmpty());
        verify(fileRepository, times(1)).findByIdAndTags(id, tags);
        verify(fileRepository, times(1)).save(file);
    }

    @Test
    void tagNotFound() {
        log.info("Test class: {}; Test method: {}; Description: {}", FileService.class.getSimpleName(), "deleteTags", "Method description");
        // arrange
        var id = "abc";
        var tags = List.of("tag1", "tag2", "tag3");
        var file = FileFactory.create("abc", "file.txt", 1000L);
        Optional<File> mockFileOptional = Optional.empty();
        when(fileRepository.findByIdAndTags(id, tags)).thenReturn(mockFileOptional);
        // act
        var executable = (Executable) () -> fileService.deleteTags(id, tags);
        // assert
        assertThrows(BadRequestException.class, executable, ExceptionMessages.TAG_NOT_FOUND);
        assertTrue(file.getTags().isEmpty());
        verify(fileRepository, times(1)).findByIdAndTags(id, tags);
        verify(fileRepository, times(0)).save(file);

    }

    @Test
    void getFiles() {
        log.info("Test class: {}; Test method: {}; Description: {}", FileService.class.getSimpleName(), "getFiles", "Method description");
        // arrange
        var page = 1;
        var size = 5;
        var tags = List.of("tag1", "tag2");
        var name = "abc";
        var pageRequest = PageRequest.of(page, size);
        var files = List.of(FileFactory.create("abc", "file.txt", 1000L, new HashSet<>(tags)));
        Page<File> mockFilesPage = new PageImpl(files);
        var expectedResponseBean = FilePageResponseBeanFactory.create(mockFilesPage);
        when(fileRepository.findAllByTagsAndNameContainingIgnoreCase(tags, name,pageRequest)).thenReturn(mockFilesPage);
        // act
        var responseBean = fileService.getFiles(page, size, tags, name);
        // assert
        assertEquals(expectedResponseBean, responseBean);
        verify(fileRepository, times(1)).findAllByTagsAndNameContainingIgnoreCase(tags, name,pageRequest);
    }

    @Test
    void getFilesByEmptyTags() {
        log.info("Test class: {}; Test method: {}; Description: {}", FileService.class.getSimpleName(), "getFiles", "Method description");
        // arrange
        var page = 1;
        var size = 5;
        List<String> tags = List.of();
        var name = "abc";
        var pageRequest = PageRequest.of(page, size);
        var files = List.of(FileFactory.create("abc", "file.txt", 1000L, new HashSet<>(tags)));
        Page<File> mockFilesPage = new PageImpl(files);
        var expectedResponseBean = FilePageResponseBeanFactory.create(mockFilesPage);
        when(fileRepository.findAllByNameContainingIgnoreCase(name, pageRequest)).thenReturn(mockFilesPage);
        // act
        var responseBean = fileService.getFiles(page, size, tags, name);
        // assert
        assertEquals(expectedResponseBean, responseBean);
        verify(fileRepository, times(1)).findAllByNameContainingIgnoreCase(name, pageRequest);
    }

    @Test
    void addTagByFileExtension() {
        log.info("Test class: {}; Test method: {}; Description: {}", FileService.class.getSimpleName(), "addTagByFileExtension", "Method description");
        // arrange
        var file = FileFactory.create("file.mp3", 100L);
        var expectedTags = Set.of("audio");
        // act
        fileService.addTagByFileExtension(file);
        // assert
        assertEquals(expectedTags, file.getTags());
    }

    @Test
    void addTagByUnknownFileExtension() {
        log.info("Test class: {}; Test method: {}; Description: {}", FileService.class.getSimpleName(), "addTagByFileExtension", "Method description");
        // arrange
        var file = FileFactory.create("file.docx", 100L);
        // act
        fileService.addTagByFileExtension(file);
        // assert
        assertTrue(file.getTags().isEmpty());
    }
}