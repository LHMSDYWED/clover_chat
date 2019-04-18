package com.wehcat.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;

/**
 * 文件上传微服务的启动类
 */
@SpringBootApplication
@EnableEurekaClient
@Configuration
public class UpLoadApplication {
    public static void main(String[] args) {
        SpringApplication.run(UpLoadApplication.class, args);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //  单个数据大小
        factory.setMaxFileSize("10240KB"); // KB,MB
        /// 总上传数据大小
        factory.setMaxRequestSize("102400KB");


        return factory.createMultipartConfig();
    }
}
