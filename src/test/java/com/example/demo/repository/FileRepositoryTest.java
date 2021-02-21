package com.example.demo.repository;

import com.example.demo.config.AppConfig;
import com.example.demo.factory.pojo.FileFactory;
import com.example.demo.pojo.File;
import com.example.demo.utils.ElasticsearchQueryUtils;
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
            FileFactory.create("ID-1","file-1.txt", List.of("tag1")),
            FileFactory.create("ID-2","file-2.txt", List.of("tag1", "tag2")),
            FileFactory.create("ID-3","file-3.txt", List.of("tag1", "tag2", "tag3")),
            FileFactory.create("ID-4","file-4.txt", List.of("tag2", "tag3")),
            FileFactory.create("ID-5","file-5.txt", List.of("tag3"))
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
    void findByIdAndTagsUsingCustomQuery() {
        log.info("Test class: {}; Test method: {}; Description: {}", FileRepository.class.getSimpleName(), "findByIdAndTagsUsingCustomQuery", "Method description");
        // arrange
        var id = "ID-3";
        var tags = List.of("tag1", "tag2");
        var tagsQueryExpr = ElasticsearchQueryUtils.getTagsQueryExpr(tags);
        var expectedFileOptional = files.stream().filter(file -> file.getId().equals(id) && file.getTags().containsAll(tags)).findFirst();
        // act
        var fileOptional = fileRepository.findByIdAndTagsUsingCustomQuery(id, tagsQueryExpr);
        // assert
        assertEquals(expectedFileOptional, fileOptional);

    }

    @Test
    void findAllByTagsUsingCustomQuery() {
        log.info("Test class: {}; Test method: {}; Description: {}", FileRepository.class.getSimpleName(), "findAllByTagsUsingCustomQuery", "Method description");
        // arrange
        var tags = List.of("tag1", "tag2");
        var tagsQueryExpr = ElasticsearchQueryUtils.getTagsQueryExpr(tags);
        var pageRequest = PageRequest.of(0, 10);
        var expectedFiles = files.stream().filter(file -> file.getTags().containsAll(tags)).collect(Collectors.toList());
        // act
        var page = fileRepository.findAllByTagsUsingCustomQuery(tagsQueryExpr, pageRequest);
        // assert
        var content = page.getContent();
        assertEquals(expectedFiles.size(), page.getTotalElements());
        assertEquals(expectedFiles.size(), content.size());
        assertTrue(expectedFiles.containsAll(content));
        assertTrue(content.containsAll(expectedFiles));
    }


}