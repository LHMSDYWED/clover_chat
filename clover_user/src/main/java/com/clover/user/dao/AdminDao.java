package com.clover.user.dao;

import com.clover.user.pojo.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author jiangxin
 * @create 2019-01-14-21:51
 * 管理员表数据访问接口
 */
public interface AdminDao extends JpaRepository<Admin,String>, JpaSpecificationExecutor<Admin> {
    /**
     * 根据登录名称获取管理员信息
     * @param loginname
     * @return
     */
    public Admin findByLoginname(String loginname);
}
