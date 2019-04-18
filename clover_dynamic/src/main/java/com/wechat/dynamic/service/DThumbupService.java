package com.wechat.dynamic.service;

import com.wechat.dynamic.dao.DThumbupDao;
import com.wechat.dynamic.pojo.DThumbup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.util.List;

/**
 * @author jiangxin
 * @create 2019-01-18-13:39
 * 服务层
 */
@Service
@Transactional  //事务
public class DThumbupService {
    @Autowired
    private DThumbupDao dThumbupDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 根据点赞者ID和被赞动态查询实体
     * @param userid
     * @param dynamicid
     * @return
     */
    public DThumbup findByUseridAndDynamicid(String userid,String dynamicid){
        return dThumbupDao.findByUseridAndDynamicid(userid,dynamicid);
    }

    /**
     * 增加
     * @param userid   传入点赞者ID和
     * @param dynamicid 被赞动态ID
     */
    public void add(String userid,String dynamicid){
        DThumbup dThumbup = new DThumbup();
        dThumbup.set_id(idWorker.nextId()+""); //设置主键
        dThumbup.setUserid(userid);
        dThumbup.setDynamicid(dynamicid);
        dThumbupDao.save(dThumbup);
    }
    /**
     * 查询全部记录
     * @return
     */
    public List<DThumbup> findAll(){
        return dThumbupDao.findAll();
    }
    /**
     * 根据主键查询实体
     * @param id
     * @return
     */
    public DThumbup findById(String id){
        DThumbup dThumbup = dThumbupDao.findById(id).get();
        return dThumbup;
    }
    /**
     * 修改
     * @param dThumbup
     */
    public void update(DThumbup dThumbup){
        dThumbupDao.save(dThumbup);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteById(String id){
        dThumbupDao.deleteById(id);
    }



}
