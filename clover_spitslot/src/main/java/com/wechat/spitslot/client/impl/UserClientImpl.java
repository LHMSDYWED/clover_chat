package com.wechat.spitslot.client.impl;


import com.wechat.spitslot.client.UserClient;
import entity.Result;
import entity.StatusCode;
import org.springframework.stereotype.Component;

@Component
public class UserClientImpl implements UserClient {

    @Override
    public Result findById(String id) {
        System.out.println("熔断去启动了");
        return new Result(false, StatusCode.REMOTEERROR,"调取服务失败");
    }
}
