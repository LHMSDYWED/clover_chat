package com.wechat.dynamic.dao;

import com.wechat.dynamic.pojo.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author jiangxin
 * @create 2019-01-18-17:52
 * 小区动态评论信息表的数据访问接口
 */
public interface CommentDao extends MongoRepository<Comment,String> {
}
