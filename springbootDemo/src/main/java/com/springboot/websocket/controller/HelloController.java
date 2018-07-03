package com.sbfirstdemo.demo.controller;

import com.sbfirstdemo.demo.domain.ContentProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Value("${name}")
    private String name;

    @Value("${content}")
    private String content;

    @Autowired
    private ContentProperties contentProperties;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String say() {
        return contentProperties.getName();
    }

    public ContentProperties getContentProperties() {
        return contentProperties;
    }

    public void setContentProperties(ContentProperties contentProperties) {
        this.contentProperties = contentProperties;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
