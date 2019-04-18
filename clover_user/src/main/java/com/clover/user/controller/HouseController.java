package com.clover.user.controller;

import com.clover.user.pojo.House;
import com.clover.user.service.HouseService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author jiangxin
 * @create 2019-01-14-22:06
 * 住房信息的控制层
 */
@RestController
@CrossOrigin
@RequestMapping("/house")
public class HouseController {
    @Autowired
    private HouseService houseService;

    @Autowired
    private HttpServletRequest request;
    /**
     * 查询全部数据
     * @return
     */
    @RequestMapping(method= RequestMethod.GET)
    public Result findAll(){
        return new Result(true,StatusCode.OK,"查询成功",houseService.findAll());
    }

    /**
     * 根据ID查询
     * @param id ID
     * @return
     */
    @RequestMapping(value="/{id}",method= RequestMethod.GET)
    public Result findById(@PathVariable String id){
        return new Result(true,StatusCode.OK,"查询成功",houseService.findById(id));
    }


    /**
     * 分页+多条件查询
     * @param searchMap 查询条件封装
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
        Page<House> pageList = houseService.findSearch(searchMap, page, size);
        return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<House>(pageList.getTotalElements(), pageList.getContent()) );
    }

    /**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",houseService.findSearch(searchMap));
    }

    /**
     * 增加
     * @param house
     */
    @RequestMapping(method=RequestMethod.POST)
    public Result add(@RequestBody House house  ){
        //必须是admin角色才可以增加
        String token = (String) request.getAttribute("claims_admin");
        if (token==null || "".equals(token)){
            return new Result(false, StatusCode.ACCESSERROR,"权限不足");
        }
        houseService.add(house);
        return new Result(true, StatusCode.OK,"增加成功");
    }

    /**
     * 修改
     * @param house
     */
    @RequestMapping(value="/{id}",method= RequestMethod.PUT)
    public Result update(@RequestBody House house, @PathVariable String id ){
        house.setId(id);
        houseService.update(house);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     * 删除
     * @param id
     */
    @RequestMapping(value="/{id}",method= RequestMethod.DELETE)
    public Result delete(@PathVariable String id ){
        houseService.deleteById(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 根据手机号查询小区用户
     * @param housenumber
     * @return
     */
    @RequestMapping(value="/housenumber/{housenumber}",method= RequestMethod.GET)
    public Result findByHousenumber(@PathVariable String housenumber){
        return new Result(false,StatusCode.OK,"查询成功",houseService.findByHousenumber(housenumber));
    }
}
