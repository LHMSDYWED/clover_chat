package com.wechat.dynamic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import util.IdWorker;
import util.JwtUtil;

/**
 * @author jiangxin
 * @create 2019-01-14-23:03
 * 小区动态微服务的启动类
 */
@SpringBootApplication
@EnableEurekaClient
public class DynamicApplication {

	public static void main(String[] args) {
		SpringApplication.run(DynamicApplication.class, args);
	}

	@Bean
	public IdWorker idWorkker(){
		return new IdWorker(1, 1);
	}

	@Bean
	public JwtUtil jwtUtil(){
		return new JwtUtil();
	}
	
}
