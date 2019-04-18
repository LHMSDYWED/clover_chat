package com.wechat.feedback.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.wechat.feedback.pojo.Feedback;
/**
 * @author jiangxin
 * @create 2019-01-14-23:30
 * 反馈信息表的数据访问接口
 */
public interface FeedbackDao extends JpaRepository<Feedback,String>,JpaSpecificationExecutor<Feedback>{
    /**
     * 根据用户id查询反馈列表，并分页
     * @param userid
     * @return
     */
    public Page<Feedback> findByUserid(String userid, Pageable pageable);

    /**
     * 根据用户ID和主键ID查询反馈的实体
     * @param userid
     * @param id
     * @return
     */
    public Feedback findByUseridAndId(String userid,String id);

    /**
     * 根据用户ID和主键ID删除反馈的实体
     * @param userid
     * @param id
     */
    public void deleteByUseridAndId(String userid,String id);
}
