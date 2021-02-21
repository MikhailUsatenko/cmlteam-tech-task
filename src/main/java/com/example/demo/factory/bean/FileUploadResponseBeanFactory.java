package com.example.demo.factory.bean;

import com.example.demo.controller.bean.FileUploadResponseBean;
import com.example.demo.pojo.File;

public class FileUploadResponseBeanFactory {

    public static FileUploadResponseBean create(File file) {
        return create(file.getId());
    }

    public static FileUploadResponseBean create(String id) {
        var bean = new FileUploadResponseBean();
        bean.setId(id);
        return bean;
    }
}
