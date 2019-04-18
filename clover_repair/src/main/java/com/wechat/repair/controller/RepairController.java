package com.wechat.repair.controller;
import com.wechat.repair.client.UserClient;
import com.wechat.repair.pojo.Repair;
import com.wechat.repair.service.RepairService;
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
 * @create 2019-01-14-23:56
 * 控制层
 */
@RestController
@CrossOrigin
@RequestMapping("/repair")
@Transactional
public class RepairController {

	@Autowired
	private RepairService repairService;

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
		return new Result(true,StatusCode.OK,"查询成功",repairService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",repairService.findById(id));
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
		Page<Repair> pageList = repairService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Repair>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",repairService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param repair type+content+images
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Repair repair  ){
		String userid = (String) request.getAttribute("id"); //获取反馈人的id
		Map<String,Object> user = (Map<String, Object>) userClient.findById(userid).getData();//获取用户信息
		repair.setRepairtime(new Date()); //保修时间
		repair.setUserid(userid); //保修人姓名
		repair.setUsername((String) user.get("name")); //保修人姓名
		repair.setMobile((String) user.get("mobile")); //保修人电话
		repair.setState(1); //状态未处理
		repairService.add(repair);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param repair
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Repair repair, @PathVariable String id ){
		repair.setId(id);
		repairService.update(repair);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		repairService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}

    /**
     * 根据用户id查询此用户所有报修内容
     * @return
     */
    @RequestMapping(value="/user",method=RequestMethod.GET)
	public Result queryByUserid(){
        String userid = (String) request.getAttribute("id");//得到用户ID
        return new Result(true,StatusCode.OK,"查询成功",repairService.queryByUserid(userid));
    }

	/**
	 * 根据用户id查询报修列表，并分页
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping(value="/user/{page}/{size}",method=RequestMethod.GET)
	public Result findByUserid(@PathVariable int page, @PathVariable int size){
		String userid = (String) request.getAttribute("id");//得到用户ID
		Page<Repair> pagelist = repairService.findByUserid(userid,page,size);
		return new Result(true,StatusCode.OK,"查询成功",new PageResult<>(pagelist.getTotalElements(),pagelist.getContent()));
	}

	/**
	 * 根据用户ID和主键ID查询保修的实体
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/user/{id}",method = RequestMethod.GET)
	public Result findByUseridAndId(@PathVariable String id){
		String userid = (String) request.getAttribute("id");//得到用户ID
		return new Result(true,StatusCode.OK,"查询成功",repairService.findByUseridAndId(userid,id));
	}

	/**
	 * 根据用户ID和处理的状态来修改，只有用户才可以
	 * @param id
	 * @param repair
	 * @return
	 */
	@RequestMapping(value = "/user/{id}",method = RequestMethod.PUT)
	public Result userUpdate(@PathVariable String id,@RequestBody Repair repair){
		//先获取用户ID
		String userid = (String) request.getAttribute("id"); //获取反馈人的id
		//根据ID和用户ID查询
		Repair repair1 = repairService.findByUseridAndId(userid, id);
		if (repair1!=null&&repair1.getState()!=1){
			return new Result(true,StatusCode.OK,"保修已在处理，您不能修改");
		}
		repair.setId(id);
		repair.setRepairtime(new Date());
		repairService.update(repair);
		return new Result(true,StatusCode.OK,"修改成功");
	}

	/**
	 * 根据用户ID和主键ID删除保修的实体
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/user/{id}",method = RequestMethod.DELETE)
	public Result deleteByUseridAndId(@PathVariable String id){
		//先获取用户ID
		String userid = (String) request.getAttribute("id"); //获取反馈人的id
		//根据ID和用户ID查询
		Repair repair1 = repairService.findByUseridAndId(userid, id);
		if (repair1!=null&&repair1.getState()!=1){
			return new Result(true,StatusCode.OK,"保修已在处理，您不能删除");
		}
		repairService.deleteByUseridAndId(userid,id);
		return new Result(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 修改处理的状态，管理员权限
	 * @param id
	 * @param repair
	 * @param state
	 * @return
	 */
	@RequestMapping(value = "/{id}/{state}",method = RequestMethod.PUT)
	public Result updateState(@PathVariable String id,@RequestBody Repair repair,@PathVariable Integer state){
		//必须是admin角色才可以修改
		String token = (String) request.getAttribute("claims_admin");
		if (token==null || "".equals(token)){
			return new Result(false, StatusCode.ACCESSERROR,"权限不足");
		}
		repair.setId(id);
		repair.setHandletime(new Date());
		repair.setState(2);
		repairService.update(repair);
		return new Result(true,StatusCode.OK,"处理成功");
	}

    /**
     * 用户确定已经维修了
     * @param id
     * @param repair
     * @return
     */
    @RequestMapping(value = "/sure/{id}",method = RequestMethod.PUT)
	public Result sureRepair(@PathVariable String id,@RequestBody Repair repair){
        //先获取用户ID
        String userid = (String) request.getAttribute("id"); //获取反馈人的id
        //根据ID和用户ID查询
        Repair repair1 = repairService.findByUseridAndId(userid, id);
        if (repair1!=null&&repair1.getState()!=2){
            return new Result(true,StatusCode.OK,"是否有哪里不对");
        }
        repair1.setId(id);
        repair1.setState(3);
        repairService.update(repair1);
        return new Result(true,StatusCode.OK,"处理成功");
    }

}
