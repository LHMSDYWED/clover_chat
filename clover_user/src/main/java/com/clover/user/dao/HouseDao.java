package com.clover.user.dao;

import com.clover.user.pojo.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author jiangxin
 * @create 2019-01-14-21:52
 * 房屋信息表数据访问接口
 */
public interface HouseDao extends JpaRepository<House,String>, JpaSpecificationExecutor<House> {
    /**
     * 通过手机号查询是否为小区用户
     * @param mobile
     * @return
     */
    public House findByMobile(String mobile);

    /**
     * 通过门牌号查询
     * @param housenumber
     * @return
     */
    public House findByHousenumber(String housenumber);
}
