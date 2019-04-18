package com.wechat.dynamic.controller;

import com.wechat.dynamic.pojo.DThumbup;
import com.wechat.dynamic.service.DThumbupService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author jiangxin
 * @create 2019-01-18-13:59
 * 控制层
 */
@RestController
@CrossOrigin
@RequestMapping("/dthumbup")
public class DThumbupController {

    @Autowired
    private DThumbupService dThumbupService;

    /**
     * 根据点赞者ID和被赞动态查询实体
     * @param dThumbup
     * @return
     */
    @RequestMapping(value = "/querry",method = RequestMethod.GET)
    public Result findByUseridAndDynamicid(@RequestBody DThumbup dThumbup){
        return new Result(true, StatusCode.OK,"查询成功",dThumbupService.findByUseridAndDynamicid(dThumbup.getUserid(),dThumbup.getDynamicid()));
    }

    /**
     * 查询全部数据
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        return new Result(true,StatusCode.OK,"查询成功",dThumbupService.findAll());
    }
    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable String id){
        return new Result(true,StatusCode.OK,"查询成功",dThumbupService.findById(id));
    }
    /**
     * 修改
     * @param dThumbup
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public Result update(@RequestBody DThumbup dThumbup,@PathVariable String id){
        dThumbup.set_id(id);
        dThumbupService.update(dThumbup);
        return new Result(true, StatusCode.OK,"修改成功");
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String id){
        dThumbupService.deleteById(id);
        return new Result(true, StatusCode.OK,"删除成功");
    }
}
