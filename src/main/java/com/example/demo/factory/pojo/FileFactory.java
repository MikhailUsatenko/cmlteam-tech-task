package com.example.demo.factory.pojo;

import com.example.demo.controller.bean.FileUploadRequestBean;
import com.example.demo.pojo.File;

import java.util.List;

public class FileFactory {

    public static File create(FileUploadRequestBean bean) {
        return create(bean.getName(), bean.getSize());
    }

    public static File create(String id, String name, long size, List<String> tags) {
        var file = new File();
        file.setId(id);
        file.setName(name);
        file.setSize(size);
        file.setTags(tags);
        return file;
    }

    public static File create(String id, String name, long size) {
        var file = new File();
        file.setId(id);
        file.setName(name);
        file.setSize(size);
        return file;
    }

    public static File create(String id, String name, List<String> tags) {
        var file = new File();
        file.setId(id);
        file.setName(name);
        file.setTags(tags);
        return file;
    }

    public static File create(String name, List<String> tags) {
        var file = new File();
        file.setName(name);
        file.setTags(tags);
        return file;
    }

    public static File create(String name, long size) {
        var file = new File();
        file.setName(name);
        file.setSize(size);
        return file;
    }
}
