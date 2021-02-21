package com.example.demo.repository;

import com.example.demo.pojo.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends ElasticsearchCrudRepository<File, String> {

    @Query("{ \"bool\": { \"must\": [ { \"term\": { \"_id\": \"?0\" }}, ?1 ] } }")
    Optional<File> findByIdAndTagsUsingCustomQuery(String id, String tagsQueryExpr);

    @Query("{ \"bool\": { \"must\": [ ?0 ] } }")
    Page<File> findAllByTagsUsingCustomQuery(String tagsQueryExpr, PageRequest pageRequest);
}
