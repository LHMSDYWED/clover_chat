package com.wechat.dynamic.dao;

import com.wechat.dynamic.pojo.DThumbup;
import org.springframework.data.mongodb.repository.MongoRepository;
/**
 * @author jiangxin
 * @create 2019-01-18-13:38
 * 小区动态点赞信息表的数据访问接口
 */
public interface DThumbupDao extends MongoRepository<DThumbup,String> {
    /**
     * 根据点赞者ID和被赞动态查询是否有点过赞
     * @param userid
     * @param dynamicid
     * @return
     */
    public DThumbup findByUseridAndDynamicid(String userid,String dynamicid);
}
