package com.example.demo.controller;

import com.example.demo.controller.api.ControllerAPI;
import com.example.demo.controller.bean.FilePageResponseBean;
import com.example.demo.controller.bean.FileUploadRequestBean;
import com.example.demo.controller.bean.FileUploadResponseBean;
import com.example.demo.controller.bean.SuccessResponseBean;
import com.example.demo.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(ControllerAPI.FILE_CONTROLLER)
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping(value = ControllerAPI.FILE_CONTROLLER_POST_UPLOAD_FILE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FileUploadResponseBean> upload(@Valid @RequestBody FileUploadRequestBean bean) {
        var responseBean = fileService.upload(bean);
        return ResponseEntity.ok(responseBean);
    }

    @DeleteMapping(value = ControllerAPI.FILE_CONTROLLER_DELETE_FILE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponseBean> delete(@PathVariable String id) {
        var responseBean = fileService.delete(id);
        return ResponseEntity.ok(responseBean);
    }

    @PostMapping(value = ControllerAPI.FILE_CONTROLLER_POST_ASSIGN_TAGS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponseBean> assignTags(@PathVariable String id, @RequestBody List<String> tags) {
        var responseBean = fileService.assignTags(id, tags);
        return ResponseEntity.ok(responseBean);
    }

    @DeleteMapping(value = ControllerAPI.FILE_CONTROLLER_DELETE_TAGS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponseBean> deleteTags(@PathVariable String id, @RequestBody List<String> tags) {
        var responseBean = fileService.deleteTags(id, tags);
        return ResponseEntity.ok(responseBean);
    }

    @GetMapping(value = ControllerAPI.FILE_CONTROLLER_GET_FILES, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FilePageResponseBean> getFiles(@RequestParam(required = false, defaultValue = "0") int page,
                                                         @RequestParam(required = false, defaultValue = "10") int size,
                                                         @RequestParam(required = false, defaultValue = "") List<String> tags,
                                                         @RequestParam(required = false, defaultValue = "", value = "q") String name) {
        var responseBean = fileService.getFiles(page, size, tags, name);
        return ResponseEntity.ok(responseBean);
    }

}
