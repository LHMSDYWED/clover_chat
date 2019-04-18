package com.wechat.repair;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import util.IdWorker;
import util.JwtUtil;

/**
 * @author jiangxin
 * @create 2019-01-14-23:45
 * 报修微服务的启动类
 */
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient              //Eureka发现其他服务需要的注解
@EnableFeignClients
public class RepairApplication {

	public static void main(String[] args) {
		SpringApplication.run(RepairApplication.class, args);
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
