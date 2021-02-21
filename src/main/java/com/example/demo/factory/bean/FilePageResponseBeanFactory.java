package com.example.demo.factory.bean;

import com.example.demo.controller.bean.FilePageResponseBean;
import com.example.demo.pojo.File;
import org.springframework.data.domain.Page;

import java.util.List;

public class FilePageResponseBeanFactory {
    public static FilePageResponseBean create(long total, List<File> files) {
        var bean = new FilePageResponseBean();
        bean.setTotal(total);
        bean.setFiles(files);
        return bean;
    }

    public static FilePageResponseBean create(Page<File> page) {
        var bean = new FilePageResponseBean();
        bean.setTotal(page.getTotalElements());
        bean.setFiles(page.getContent());
        return bean;
    }
}
