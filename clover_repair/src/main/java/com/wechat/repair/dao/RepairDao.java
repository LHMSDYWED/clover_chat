package com.wechat.repair.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.wechat.repair.pojo.Repair;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author jiangxin
 * @create 2019-01-14-23:50
 * 报修信息表的数据访问接口
 */
public interface RepairDao extends JpaRepository<Repair,String>,JpaSpecificationExecutor<Repair>{
    /**
     * 根据用户id查询保修列表，并分页
     * @param userid
     * @param pageable
     * @return
     */
    public Page<Repair> findByUserid(String userid, Pageable pageable);

    /**
     * 根据用户ID和主键ID查询保修的实体
     * @param userid
     * @param id
     * @return
     */
    public Repair findByUseridAndId(String userid,String id);

    /**
     * 根据用户ID和主键ID删除保修的实体
     * @param userid
     * @param id
     */
    public void deleteByUseridAndId(String userid,String id);


    /**
     * 根据用户id查询此用户所有报修内容
     * @param userid
     * @return
     */
    public List<Repair> findByUserid(String userid);


}
