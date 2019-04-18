package com.clover.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import util.IdWorker;
import util.JwtUtil;

/**
 * @author jiangxin
 * @create 2019-01-14-21:40
 * 用户微服务的启动类
 */
@SpringBootApplication
@EnableEurekaClient
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

    @Bean
    public IdWorker idWorkker(){
        return new IdWorker(1, 1);
    }
    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil();
    }
}
