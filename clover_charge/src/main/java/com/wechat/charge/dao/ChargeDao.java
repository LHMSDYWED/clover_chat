package com.wechat.charge.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.wechat.charge.pojo.Charge;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author jiangxin
 * @create 2019-01-14-22:49
 * 费用信息表的数据访问接口
 */
public interface ChargeDao extends JpaRepository<Charge,String>,JpaSpecificationExecutor<Charge>{
//    Page<Charge> findByHoserid(String houseid, Pageable pageable, Sort by);

    /**
     * 根据门牌id查询所有 按时间和状态排序
     * @param houseid
     * @return
     */
    public Page<Charge> findByHoserid(String houseid, Pageable pageable);


}
