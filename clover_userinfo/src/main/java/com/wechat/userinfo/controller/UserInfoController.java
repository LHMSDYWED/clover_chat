package com.wechat.userinfo.controller;

import com.wechat.userinfo.pojo.UserInfo;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author jiangxin
 * @create 2019-01-25-20:49
 * 微信微服务的控制层
 */
@RestController
@CrossOrigin
@RequestMapping("/userinfo")
public class UserInfoController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Map<String,Object> map;

    @Autowired
    private Environment env;

    /**
     * 获取微信用户信息的方法
     * @param code
     * @return
     */
    @RequestMapping(value = "/{code}",method = RequestMethod.GET)
    public Result getUserInfo(@PathVariable String code){
        String appid = env.getProperty("wechat.appid");   //小程序 appId
        String secret = env.getProperty("wechat.secret"); //小程序 appSecret
        String grantType = env.getProperty("wechat.grant-type"); //授权类型，此处只需填写 authorization_code
        map.put("appid",appid);
        map.put("secret",secret);
        map.put("code",code);
        map.put("grantType",grantType);
        UserInfo userInfo = restTemplate.getForObject("https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={code}&grant_type={grantType}", UserInfo.class,map);
        return new Result(true, StatusCode.OK,"获取微信信息成功",userInfo);
    }
}
