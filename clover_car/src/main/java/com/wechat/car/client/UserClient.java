package com.wechat.car.client;


import com.wechat.car.client.impl.UserClientImpl;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
@FeignClient(value = "clover-user",fallback = UserClientImpl.class)   //熔断器需要的注解
public interface UserClient {
    @RequestMapping(value = "/user/mobile/{mobile}", method = RequestMethod.POST)
    public Result findByMobile(@PathVariable String mobile);

}
