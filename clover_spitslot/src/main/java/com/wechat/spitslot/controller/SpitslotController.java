package com.wechat.spitslot.controller;

import com.wechat.spitslot.client.UserClient;
import com.wechat.spitslot.pojo.Spitslot;
import com.wechat.spitslot.service.SpitslotService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.hibernate.validator.internal.util.IdentitySet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author jiangxin
 * @create 2019-01-15-00:18
 * 控制层
 */
@RestController
@CrossOrigin
@RequestMapping("/spitslot")
@Transactional
public class SpitslotController {

	@Autowired
	private SpitslotService spitslotService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private UserClient userClient;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	public Map<String,Object> map;

	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",spitslotService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		String userid = (String) request.getAttribute("id"); //获取点赞人的id
		Spitslot spitslot = spitslotService.findById(id);
		if (redisTemplate.opsForValue().get("thumbup_"+userid+"_"+id)!=null){
			spitslot.setFlag(true);
		}else {
			spitslot.setFlag(false);
		}
		return new Result(true,StatusCode.OK,"查询成功",spitslot);
	}

	
	/**
	 * 增加
	 * @param spitslot 有父级id要传入进来
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Spitslot spitslot  ){
		String userid = (String) request.getAttribute("id"); //获取发布人的id
		Map<String,Object> user = (Map) userClient.findById(userid).getData();
		spitslot.setUserid(userid);//设置发布人id
		spitslot.setNickname((String) user.get("nickname"));//获取发布人的昵称
		spitslotService.add(spitslot);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param spitslot
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Spitslot spitslot, @PathVariable String id ){
		spitslot.set_id(id);
		spitslotService.update(spitslot);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		spitslotService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 根据上级ID查询吐槽分页数据
	 * @param parentid
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping(value="/comment/{parentid}/{page}/{size}",method=RequestMethod.GET)
	public Result findByParentid(@PathVariable String parentid,@PathVariable int page,@PathVariable int size){
		Page<Spitslot> pageList =spitslotService.findByParentid(parentid,page,size);
		return new Result(true,StatusCode.OK,"查询成功",new PageResult<Spitslot>(pageList.getTotalElements(),pageList.getContent()));
	}
	/**

	 * 点赞
	 * @return
	 */
	@RequestMapping(value = "/thumbup/{id}", method = RequestMethod.PUT)
	public Result thumbup(@PathVariable String id){
		String userid = (String) request.getAttribute("id"); //获取点赞人的id
		//判断用户是否点过赞
		if (redisTemplate.opsForValue().get("thumbup_"+userid+"_"+id)!=null){
			//取消点赞minusthumbup
			spitslotService.minusthumbup(id);
			//清除redishuancun
			redisTemplate.delete("thumbup_"+userid+"_"+id);
			return new Result(true,StatusCode.OK,"取消赞成功");
		}
		//没有就点赞
		spitslotService.addthumbup(id);
		//存一份到redis,用来做标记
		redisTemplate.opsForValue().set("thumbup_"+userid+"_"+id,1);
		return new Result(true,StatusCode.OK,"点赞成功");
	}


    /**
     * 查询所有，分页
     * @param page
     * @param size
     * @return
     */
	@RequestMapping(value = "/pageable/{page}/{size}",method = RequestMethod.GET)
	public Result querryAll(@PathVariable int page,@PathVariable int size){
		String userid = (String) request.getAttribute("id"); //获取点赞人的id
        Page<Spitslot> pageList =spitslotService.querryAll(page,size);
		List<Spitslot> spitslot = pageList.getContent();
		for (int i = 0;i<spitslot.size();i++){
			String id = spitslot.get(i).get_id();
			if (redisTemplate.opsForValue().get("thumbup_"+userid+"_"+id)!=null){
				spitslot.get(i).setFlag(true);
			}else {
				spitslot.get(i).setFlag(false);
			}
		}
		return new Result(true,StatusCode.OK,"查询成功",new PageResult<Spitslot>(pageList.getTotalElements(),spitslot));
    }
	/**
	 * 条件查询并分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
		@RequestMapping(value = "/whereMap/{page}/{size}",method = RequestMethod.POST)
	public Result findSearch(@RequestBody Map whereMap , @PathVariable int page, @PathVariable int size){
			System.out.println(whereMap.get("querryMap"));
			String userid = (String) request.getAttribute("id"); //获取点赞人的id
			Page<Spitslot> pageList =spitslotService.findSearch(whereMap, page, size);
			List<Spitslot> spitslot = pageList.getContent();
			for (int i = 0;i<spitslot.size();i++){
				String id = spitslot.get(i).get_id();
				if (redisTemplate.opsForValue().get("thumbup_"+userid+"_"+id)!=null){
					spitslot.get(i).setFlag(true);
				}else {
					spitslot.get(i).setFlag(false);
				}
			}
			return new Result(true,StatusCode.OK,"查询成功",new PageResult<Spitslot>(pageList.getTotalElements(),spitslot));
//		}
//		Page<Spitslot> pageList = spitslotService.findSearch(searchMap, page, size);
//		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Spitslot>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
	 * 根据userid查询信息并分页
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping(value="/user/{page}/{size}",method=RequestMethod.GET)
	public Result findByUserid(@PathVariable int page,@PathVariable int size){
		String userid = (String) request.getAttribute("id"); //获取用户的id
		Page<Spitslot> pageList =spitslotService.findByUserid(userid,page,size);
		return new Result(true,StatusCode.OK,"查询成功",new PageResult<Spitslot>(pageList.getTotalElements(),pageList.getContent()));
	}
}
