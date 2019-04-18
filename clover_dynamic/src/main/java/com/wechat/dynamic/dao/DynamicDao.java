package com.wechat.dynamic.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.wechat.dynamic.pojo.Dynamic;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author jiangxin
 * @create 2019-01-14-23:10
 * 小区动态表数据访问接口
 */
public interface DynamicDao extends JpaRepository<Dynamic,String>,JpaSpecificationExecutor<Dynamic>{
    /**
     * 增加点赞数
     * @param id
     */
    @Modifying
    @Query(value = "UPDATE tb_dynamic SET thumbup=thumbup+1 WHERE id=?",nativeQuery = true)
    public void addThumbup(String id);

    /**
     * 减少点赞数
     * @param id
     */
    @Modifying
    @Query(value = "UPDATE tb_dynamic SET thumbup=thumbup-1 WHERE id=?",nativeQuery = true)
    public void minusThumbup(String id);

    /**
     * 增加评论数
     * @param id
     */
    @Modifying
    @Query(value = "UPDATE tb_dynamic SET comment=comment+1 WHERE id=?",nativeQuery = true)
    public void addComment(String id);

    /**
     * 减少评论数
     * @param id
     */
    @Modifying
    @Query(value = "UPDATE tb_dynamic SET comment=comment-1 WHERE id=?",nativeQuery = true)
    public void minusComment(String id);

}
