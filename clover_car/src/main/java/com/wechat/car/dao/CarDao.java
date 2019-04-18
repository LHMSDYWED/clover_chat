package com.wechat.car.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.wechat.car.pojo.Car;
/**
 * @author jiangxin
 * @create 2019-01-14-22:25
 * 车位、车辆信息表访问数据的接口
 */
public interface CarDao extends JpaRepository<Car,String>,JpaSpecificationExecutor<Car>{
    /**
     * 根据手机号和车牌号查询用户
     * @param mobile
     * @param carid
     * @return
     */
    public Car findByMobileAndCarid(String mobile,String carid); //select * from car where mobile = ? and carid =?
}
