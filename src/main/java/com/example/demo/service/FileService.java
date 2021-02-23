package com.example.demo.service;

import com.example.demo.controller.bean.FilePageResponseBean;
import com.example.demo.controller.bean.FileUploadRequestBean;
import com.example.demo.controller.bean.FileUploadResponseBean;
import com.example.demo.controller.bean.SuccessResponseBean;
import com.example.demo.exception.message.ExceptionMessages;
import com.example.demo.factory.bean.FilePageResponseBeanFactory;
import com.example.demo.factory.bean.FileUploadResponseBeanFactory;
import com.example.demo.factory.bean.SuccessResponseBeanFactory;
import com.example.demo.factory.exception.BadRequestExceptionFactory;
import com.example.demo.factory.exception.NotFoundExceptionFactory;
import com.example.demo.factory.pojo.FileFactory;
import com.example.demo.pojo.File;
import com.example.demo.repository.FileRepository;
import com.example.demo.utils.JacksonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.net.URLConnection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final FileRepository fileRepository;

    public FileUploadResponseBean upload(FileUploadRequestBean bean) {
        log.info("Got request to upload file by bean: {}", JacksonUtils.getJson(bean));
        var file = FileFactory.create(bean);
        addTagByFileExtension(file);
        return FileUploadResponseBeanFactory.create(fileRepository.save(file));
    }

    public SuccessResponseBean delete(String id) {
        log.info("Got request to delete file by id: {}", id);
        return fileRepository.findById(id).map(file -> {
            fileRepository.delete(file);
            return SuccessResponseBeanFactory.create();
        }).orElseThrow(() -> NotFoundExceptionFactory.create(ExceptionMessages.FILE_NOT_FOUND));
    }

    public SuccessResponseBean assignTags(String id, List<String> tags) {
        log.info("Got request to assign tags to file by id: {}, tags: {}", id, JacksonUtils.getJson(tags));
        return fileRepository.findById(id).map(file -> {
            file.getTags().addAll(tags);
            fileRepository.save(file);
            return SuccessResponseBeanFactory.create();
        }).orElseThrow(() -> NotFoundExceptionFactory.create(ExceptionMessages.FILE_NOT_FOUND));
    }

    public SuccessResponseBean deleteTags(String id, List<String> tags) {
        log.info("Got request to remove tags from file by id: {}, tags: {}", id, JacksonUtils.getJson(tags));
        return fileRepository.findByIdAndTags(id, tags).map(file -> {
            file.getTags().removeAll(tags);
            fileRepository.save(file);
            return SuccessResponseBeanFactory.create();
        }).orElseThrow(() -> BadRequestExceptionFactory.create(ExceptionMessages.TAG_NOT_FOUND));
    }

    public FilePageResponseBean getFiles(int page, int size, List<String> tags, String name) {
        log.info("Got request get files page by page: {}, size: {}, tags: {}, name: {}", page, size, JacksonUtils.getJson(tags), name);
        var pageRequest = PageRequest.of(page, size);
        if (tags.isEmpty()) {
            return FilePageResponseBeanFactory.create(fileRepository.findAllByNameContainingIgnoreCase(name, pageRequest));
        } else {
            return FilePageResponseBeanFactory.create(fileRepository.findAllByTagsAndNameContainingIgnoreCase(tags, name, pageRequest));
        }
    }

    public void addTagByFileExtension(File file) {
        var fileName = file.getName();
        var tags = file.getTags();
        var fileNameMap = URLConnection.getFileNameMap();
        var mimeType = fileNameMap.getContentTypeFor(fileName);
        Optional.ofNullable(mimeType).map(mimeTypeValue -> mimeTypeValue.split("/")[0]).ifPresent(tags::add);
    }
}