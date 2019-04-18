package com.wechat.feedback.controller;
import com.wechat.feedback.client.UserClient;
import com.wechat.feedback.pojo.Feedback;
import com.wechat.feedback.service.FeedbackService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
/**
 * @author jiangxin
 * @create 2019-01-14-23:38
 * 控制层
 */
@RestController
@CrossOrigin
@RequestMapping("/feedback")
@Transactional
public class FeedbackController {

	@Autowired
	private FeedbackService feedbackService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private UserClient userClient;
	
	
	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",feedbackService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",feedbackService.findById(id));
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
		Page<Feedback> pageList = feedbackService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Feedback>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",feedbackService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param feedback type+content+images
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Feedback feedback  ){
		String userid = (String) request.getAttribute("id"); //获取反馈人的id
		Map<String,Object> user = (Map<String, Object>) userClient.findById(userid).getData();//获取用户信息
		feedback.setUserid(userid); //设置反馈人的ID
		feedback.setUsername((String) user.get("name")); //设置反馈人的姓名
		feedback.setMobile((String) user.get("mobile")); //设置反馈人的电话
		feedback.setBacktime(new Date()); //设置反馈的时间
		feedback.setState(1); //设置状态为未处理
		feedbackService.add(feedback);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param feedback
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Feedback feedback, @PathVariable String id ){
		feedback.setId(id);
		feedbackService.update(feedback);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		feedbackService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 根据用户id查询反馈列表，并分页
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping(value="/user/{page}/{size}",method=RequestMethod.GET)
	public Result findByUserid(@PathVariable int page, @PathVariable int size){
		String userid = (String) request.getAttribute("id");//得到用户ID
		Page<Feedback> pagelist = feedbackService.findByUserid(userid,page,size);
		return new Result(true,StatusCode.OK,"查询成功",new PageResult<>(pagelist.getTotalElements(),pagelist.getContent()));
	}

	/**
	 * 根据用户ID和主键ID查询反馈的实体
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/user/{id}",method = RequestMethod.GET)
	public Result findByUseridAndId(@PathVariable String id){
		String userid = (String) request.getAttribute("id");//得到用户ID
		return new Result(true,StatusCode.OK,"查询成功",feedbackService.findByUseridAndId(userid,id));
	}

	/**
	 * 根据用户ID和处理的状态来修改，只有用户才可以
	 * @return
	 */
	@RequestMapping(value = "/user/{id}",method = RequestMethod.PUT)
	public Result userUpdate(@PathVariable String id,@RequestBody Feedback feedback){
		//先获取用户ID
		String userid = (String) request.getAttribute("id"); //获取反馈人的id
		//根据ID和用户ID查询
		Feedback feedback1 = feedbackService.findByUseridAndId(userid, id);
		//判断状态
		if (feedback1.getState()!=1){
			return new Result(true,StatusCode.OK,"反馈已在处理，您不能修改");
		}
		feedback.setId(id);
		feedback.setBacktime(new Date());
		feedbackService.update(feedback);
		return new Result(true,StatusCode.OK,"修改成功");
	}

	/**
	 * 根据用户ID和主键ID删除反馈的实体
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/user/{id}",method = RequestMethod.DELETE)
	public Result deleteByUseridAndId(@PathVariable String id){
		//先获取用户ID
		String userid = (String) request.getAttribute("id"); //获取反馈人的id
		//根据ID和用户ID查询
		Feedback feedback1 = feedbackService.findByUseridAndId(userid, id);
		//判断状态
		if (feedback1.getState()!=1){
			return new Result(true,StatusCode.OK,"反馈已在处理，您不能删除");
		}
		feedbackService.deleteByUseridAndId(userid,id);
		return new Result(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 修改处理的状态，管理员权限
	 * @param id
	 * @param feedback
	 * @param state
	 * @return
	 */
	@RequestMapping(value = "/{id}/{state}",method = RequestMethod.PUT)
	public Result updateState(@PathVariable String id,@RequestBody Feedback feedback,@PathVariable Integer state){
		//必须是admin角色才可以修改
		String token = (String) request.getAttribute("claims_admin");
		if (token==null || "".equals(token)){
			return new Result(false, StatusCode.ACCESSERROR,"权限不足");
		}
		feedback.setId(id);
		feedback.setHandletime(new Date());//处理的时间
		feedback.setState(2);//标记为已处理;
		feedbackService.update(feedback);
		return new Result(true,StatusCode.OK,"处理成功");
	}
	
}
