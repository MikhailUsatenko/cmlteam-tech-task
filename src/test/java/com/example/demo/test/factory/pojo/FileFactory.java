package com.example.demo.test.factory.pojo;

import com.example.demo.controller.bean.FileUploadRequestBean;
import com.example.demo.pojo.File;

import java.util.Optional;
import java.util.Set;

public class FileFactory {
    public static File create(FileUploadRequestBean bean) {
        return create(bean.getName(), bean.getSize());
    }

    public static File create(String id, String name, long size, Set<String> tags) {
        var file = new File();
        file.setId(id);
        file.setName(name);
        file.setSize(size);
        Optional.ofNullable(tags).ifPresent(file::setTags);
        return file;
    }

    public static File create(String id, String name, long size) {
        return create(id, name, size, null);
    }

    public static File create(String id, String name, Set<String> tags) {
        return create(id, name, 0L, tags);
    }

    public static File create(String name, long size) {
        return create(null, name, size, null);
    }
}
