package com.wechat.feedback.client;




import com.wechat.feedback.client.impl.UserClientImpl;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
@FeignClient(value = "clover-user",fallback = UserClientImpl.class)
public interface UserClient {

    /**
     * 根据id号查询user实体
     * @param id
     * @return
     */
    @RequestMapping(value="/user/{id}",method= RequestMethod.GET)
    public Result findById(@PathVariable String id);
}
