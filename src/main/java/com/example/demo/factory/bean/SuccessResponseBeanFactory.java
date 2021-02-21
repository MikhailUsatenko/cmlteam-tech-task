package com.example.demo.factory.bean;

import com.example.demo.controller.bean.SuccessResponseBean;

public class SuccessResponseBeanFactory {

    public static SuccessResponseBean create() {
        var bean = new SuccessResponseBean();
        bean.setSuccess(true);
        return bean;
    }
}
