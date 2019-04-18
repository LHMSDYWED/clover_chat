package com.wechat.dynamic.controller;
import com.wechat.dynamic.pojo.DThumbup;
import com.wechat.dynamic.pojo.Dynamic;
import com.wechat.dynamic.service.DThumbupService;
import com.wechat.dynamic.service.DynamicService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
/**
 * @author jiangxin
 * @create 2019-01-14-23:18
 * 控制层
 */
@RestController
@CrossOrigin
@RequestMapping("/dynamic")
@Transactional  //事务
public class DynamicController {

	@Autowired
	private DynamicService dynamicService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private DThumbupService dThumbupService;

	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",dynamicService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",dynamicService.findById(id));
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
		Page<Dynamic> pageList = dynamicService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Dynamic>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",dynamicService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param dynamic  theme+content+images
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Dynamic dynamic  ){
		//必须是admin角色才可以增加
		String token = (String) request.getAttribute("claims_admin");
		if (token==null || "".equals(token)){
			return new Result(false, StatusCode.ACCESSERROR,"权限不足");
		}
		dynamic.setCreatetime(new Date()); //发布时间
		dynamic.setUpdatetime(new Date());	//修改时间
		dynamic.setIstop(1);  //新发布都是置顶
		dynamic.setState(1); //发布都是提前，所以这里是未开始
		dynamic.setVisits(0); //初始化数据
		dynamic.setThumbup(0); //初始化数据
		dynamic.setComment(0);
		dynamicService.add(dynamic);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param dynamic
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Dynamic dynamic, @PathVariable String id ){
		//必须是admin角色才可以增加
		String token = (String) request.getAttribute("claims_admin");
		if (token==null || "".equals(token)){
			return new Result(false, StatusCode.ACCESSERROR,"权限不足");
		}
		dynamic.setId(id);
		dynamicService.update(dynamic);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		//必须是admin角色才可以增加
		String token = (String) request.getAttribute("claims_admin");
		if (token==null || "".equals(token)){
			return new Result(false, StatusCode.ACCESSERROR,"权限不足");
		}
		dynamicService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 改变点赞数据
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/thumbup/{id}",method= RequestMethod.PUT)
	public Result toggleThumbup(@PathVariable String id){
		//先查询有没点过赞
		String userid = (String) request.getAttribute("id");
		DThumbup dThumbup = dThumbupService.findByUseridAndDynamicid(userid, id);
		System.out.println(dThumbup);
		if (dThumbup !=null) {
			//删除点赞信息数据
			dThumbupService.deleteById(dThumbup.get_id());
			//点赞数-1
			dynamicService.minusThumbup(id);
			return new Result(true,StatusCode.OK,"取消赞成功");
		}
		//增加点赞信息数据
		dThumbupService.add(userid,id);
		dynamicService.addThumbup(id);
		return new Result(true,StatusCode.OK,"点赞成功");
	}

	/**
	 * 查询所有并分页
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping(value="/findAllPage/{page}/{size}",method=RequestMethod.GET)
	public Result findAllPage(@PathVariable int page, @PathVariable int size){
		Page<Dynamic> pageList = dynamicService.findAllPage(page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Dynamic>(pageList.getTotalElements(), pageList.getContent()) );
	}
	
}
