package com.wechat.wechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author jiangxin
 * @create 2019-01-21-15:33
 * 微信小程序网关微服务的启动类
 */
@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
public class WechatApplication {
    public static void main(String[] args) {
        SpringApplication.run(WechatApplication.class);
    }
}
