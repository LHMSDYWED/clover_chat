package com.wechat.sms.listener;

import com.aliyuncs.exceptions.ClientException;
import com.wechat.sms.utils.JuheUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "juhe")
public class JuheListener {

    @Autowired
    private JuheUtil juheUtil;
    @RabbitHandler
    public void executeSms(Map<String, String> map){
        String mobile = map.get("mobile");
        String checkcode = map.get("checkcode");
        System.out.println("手机号："+map.get("mobile"));
        System.out.println("验证码："+map.get("checkcode"));
        try {
            juheUtil.getRequest2(mobile,checkcode);
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
