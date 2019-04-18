package com.wechat.dynamic.service;

import com.wechat.dynamic.dao.CommentDao;
import com.wechat.dynamic.pojo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;
import java.util.List;

/**
 * @author jiangxin
 * @create 2019-01-18-17:57
 * 服务层
 */
@Service
@Transactional  //事务
public class CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 查询全部记录
     * @return
     */
    public List<Comment> findAll(){
        return commentDao.findAll();
    }
    /**
     * 根据主键查询实体
     * @param id
     * @return
     */
    public Comment findById(String id){
        Comment comment = commentDao.findById(id).get();
        return comment;
    }
    /**
     * 修改
     * @param comment
     */
    public void update(Comment comment){
        commentDao.save(comment);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteById(String id){
        commentDao.deleteById(id);
    }

    /**
     * 增加评论
     * @param comment
     */
    public void add(Comment comment){
        comment.set_id(idWorker.nextId()+"");
        commentDao.save(comment);
    }
}
