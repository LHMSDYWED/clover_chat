package com.wechat.userinfo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class UpLoadConfiguration extends WebMvcConfigurationSupport {
    @Autowired
    HttpServletRequest request;
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
       registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/**");
       registry.addResourceHandler("/images/**").addResourceLocations("file:D:/images/");
    }
}
