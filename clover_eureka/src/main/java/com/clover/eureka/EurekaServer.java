package com.clover.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer         //Eureka服务端需要的注解
public class EurekaServer {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServer.class);
    }
}
