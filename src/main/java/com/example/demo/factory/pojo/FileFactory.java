package com.example.demo.factory.pojo;

import com.example.demo.controller.bean.FileUploadRequestBean;
import com.example.demo.pojo.File;

public class FileFactory {

    public static File create(FileUploadRequestBean bean) {
        return create(bean.getName(), bean.getSize());
    }

    public static File create(String name, long size) {
        var file = new File();
        file.setName(name);
        file.setSize(size);
        return file;
    }
}
