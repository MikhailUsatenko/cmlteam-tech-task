package com.example.demo.repository;

import com.example.demo.pojo.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends ElasticsearchRepository<File, String> {

    Optional<File> findByIdAndTags(String id, List<String> tags);

    Page<File> findAllByNameContainingIgnoreCase(String name, PageRequest pageRequest);

    Page<File> findAllByTagsAndNameContainingIgnoreCase(List<String> tags, String name, PageRequest pageRequest);

}
