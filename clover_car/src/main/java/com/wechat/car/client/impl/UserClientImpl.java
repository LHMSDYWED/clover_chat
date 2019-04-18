package com.wechat.car.client.impl;


import com.wechat.car.client.UserClient;
import entity.Result;
import entity.StatusCode;
import org.springframework.stereotype.Component;

@Component
public class UserClientImpl implements UserClient {

    @Override
    public Result findByMobile(String mobile) {
        System.out.println("熔断去启动了");
        return new Result(false, StatusCode.REMOTEERROR,"调取服务失败");
    }
}
