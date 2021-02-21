package com.example.demo.factory.bean;

import com.example.demo.controller.bean.FileUploadRequestBean;

public class FileUploadRequestBeanFactory {

    public static FileUploadRequestBean create(String name, Long size) {
        var bean = new FileUploadRequestBean();
        bean.setName(name);
        bean.setSize(size);
        return bean;
    }
}
