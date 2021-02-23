package com.example.demo.repository;

import com.example.demo.config.AppConfig;
import com.example.demo.pojo.File;
import com.example.demo.test.factory.pojo.FileFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Embedded Elasticsearch configuration not used.
 * Run Elasticsearch on Docker before tests: docker run -d --name es762 -p 9200:9200 -e "discovery.type=single-node" elasticsearch:7.6.2
 *
 * --- Embedded Elasticsearch configuration (JUnit 4):
 *
 *     @ClassRule
 *     public static ElasticsearchContainer container = new ElasticsearchContainer();
 *
 *     @BeforeClass
 *     public static void setUpAll() {
 *         System.setProperty("spring.data.elasticsearch.cluster-nodes", container.getContainerIpAddress() + ":" + container.getMappedPort(9300));
 *     }
 *
 * ---
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = { AppConfig.class })
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class FileRepositoryTest {

    private final FileRepository fileRepository;

    private static final List<File> files = List.of(
            FileFactory.create("ID-1","file-aaaaaaa.txt", Set.of("tag1")),
            FileFactory.create("ID-2","file-bbbb aaa ccc.txt", Set.of("tag1", "tag2")),
            FileFactory.create("ID-3","file-AaAA.txt", Set.of("tag1", "tag2", "tag3")),
            FileFactory.create("ID-4","file-4.txt", Set.of("tag2", "tag3")),
            FileFactory.create("ID-5","file-5.txt", Set.of("tag3"))
    );

    @BeforeEach
    void setUp() {
        fileRepository.deleteAll();
        fileRepository.saveAll(files);
    }

    @AfterEach
    void tearDown() {
        fileRepository.deleteAll();
    }

    @Test
    void findByIdAndTags() {
        log.info("Test class: {}; Test method: {}; Description: {}", FileRepository.class.getSimpleName(), "findByIdAndTags", "Method description");
        // arrange
        var id = "ID-3";
        var tags = List.of("tag1", "tag2");
        var expectedFileOptional = files.stream().filter(file -> file.getTags().containsAll(tags) && file.getId().equals(id)).findFirst();
        // act
        var fileOptional = fileRepository.findByIdAndTags(id, tags);
        // assert
        assertEquals(expectedFileOptional, fileOptional);
    }

    @Test
    void findAllByNameContainingIgnoreCase() {
        log.info("Test class: {}; Test method: {}; Description: {}", FileRepository.class.getSimpleName(), "findAllByNameContainingIgnoreCase", "Method description");
        // arrange
        var name = "aaa";
        var pageRequest = PageRequest.of(0, 10);
        var expectedFiles = files.stream().filter(file -> file.getName().toUpperCase().contains(name.toUpperCase())).collect(Collectors.toList());
        // act
        var page = fileRepository.findAllByNameContainingIgnoreCase(name, pageRequest);
        // assert
        var content = page.getContent();
        assertEquals(expectedFiles.size(), page.getTotalElements());
        assertEquals(expectedFiles.size(), content.size());
        assertTrue(expectedFiles.containsAll(content));
        assertTrue(content.containsAll(expectedFiles));
    }

    @Test
    void findAllByTagsAndNameContainingIgnoreCase() {
        log.info("Test class: {}; Test method: {}; Description: {}", FileRepository.class.getSimpleName(), "findAllByTagsAndNameContainingIgnoreCase", "Method description");
        // arrange
        var name = "aaa";
        var tags = List.of("tag1", "tag2");
        var pageRequest = PageRequest.of(0, 10);
        var expectedFiles = files.stream().filter(file -> isFilenameContainsIgnoreCase(file, name) && file.getTags().containsAll(tags)).collect(Collectors.toList());
        // act
        var page = fileRepository.findAllByTagsAndNameContainingIgnoreCase(tags, name, pageRequest);
        // assert
        var content = page.getContent();
        assertEquals(expectedFiles.size(), page.getTotalElements());
        assertEquals(expectedFiles.size(), content.size());
        assertTrue(expectedFiles.containsAll(content));
        assertTrue(content.containsAll(expectedFiles));
    }

    private boolean isFilenameContainsIgnoreCase(File file, String name) {
        return file.getName().toUpperCase().contains(name.toUpperCase());
    }
}