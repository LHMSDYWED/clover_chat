package com.clover.user.dao;

import com.clover.user.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author jiangxin
 * @create 2019-01-14-21:50
 * 用户表数据访问接口
 */
public interface UserDao extends JpaRepository<User,String>, JpaSpecificationExecutor<User> {

    /**
     * 根据手机号查询用户
     * @param mobile
     * @return
     */
    public User findByMobile(String mobile);

    /**
     * 根据微信号唯一编码查询用户实体
     * @param openid
     * @return
     */
    public User findByOpenid(String openid);

//    @Modifying
//    @Query(value = "",nativeQuery = true)
//    public void registerById();
}
