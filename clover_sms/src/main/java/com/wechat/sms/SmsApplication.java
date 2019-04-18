package com.wechat.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jiangxin
 * @create 2019-01-15-09:47
 * 短信微服务的启动类
 */
@SpringBootApplication
public class SmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmsApplication.class);
    }
}
