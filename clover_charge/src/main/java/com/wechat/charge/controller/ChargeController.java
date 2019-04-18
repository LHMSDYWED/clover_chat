package com.wechat.charge.controller;
import com.wechat.charge.client.UserClient;
import com.wechat.charge.pojo.Charge;
import com.wechat.charge.service.ChargeService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
/**
 * @author jiangxin
 * @create 2019-01-14-22:55
 * 服务层
 */
@RestController
@CrossOrigin
@RequestMapping("/charge")
public class
ChargeController {

	@Autowired
	private ChargeService chargeService;

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
		return new Result(true,StatusCode.OK,"查询成功",chargeService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",chargeService.findById(id));
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
		Page<Charge> pageList = chargeService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Charge>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",chargeService.findSearch(searchMap));
    }
	
	/**
	 * 增加费用信息的方法，前台传入门牌号，各个费用即可
	 * @param charge
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Charge charge  ){
		//必须是admin角色才可以增加
		String token = (String) request.getAttribute("claims_admin");
		if (token==null || "".equals(token)){
			return new Result(false, StatusCode.ACCESSERROR,"权限不足");
		}
		//必须要有该门牌号
		Result result = userClient.findByHousenumber(charge.getHousenumber());
		if (result.getData()==null){
			return new Result(false, StatusCode.ERROR,"不存在该门牌号");
		}
		Map<String,Object> data = (Map) result.getData();
//		System.out.println(data.get("id")); //获取数据的方法
		charge.setHoserid((String) data.get("id"));
		charge.setTotalcharge(charge.getEnergycharge()+charge.getEstatecharge()+charge.getWatercharge()+charge.getGascharge());
		chargeService.add(charge);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param id 费用信息的ID，前台需隐藏显示
	 * @param charge  整个信息
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Charge charge, @PathVariable String id ){
		//必须是admin角色才可以增加
		String token = (String) request.getAttribute("claims_admin");
		if (token==null || "".equals(token)){
			return new Result(false, StatusCode.ACCESSERROR,"权限不足");
		}
		charge.setId(id);
		chargeService.update(charge);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id 费用信息的ID，前台需隐藏显示
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		//必须是admin角色才可以增加
		String token = (String) request.getAttribute("claims_admin");
		if (token==null || "".equals(token)){
			return new Result(false, StatusCode.ACCESSERROR,"权限不足");
		}
		chargeService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 支付费用
	 * @param charge  显示的基本信息+支付人姓名和电话号码+支付金额【把支付金额赋值给前台的Totalcharge】
	 * @param id  费用信息的ID，前台需隐藏显示
	 * @return
	 */
	@RequestMapping(value="/pay/{id}",method= RequestMethod.PUT)
	public Result payCharge(@RequestBody Charge charge, @PathVariable String id){
		//从验证中获取当前执行用户的ID
		String userId = (String) request.getAttribute("id");
		//调用用户微服务的方法，获取用户信息
		Map<String,Object> data = (Map<String, Object>) userClient.findById(userId).getData();
		//更加id查询出需支付的费用
		Charge charge1 = chargeService.findById(id);
		double totalCharge = charge1.getTotalcharge();
		System.out.println(charge.getTotalcharge());
		System.out.println(totalCharge);
		//都不能为空，防止空指针
		if (charge.getTotalcharge()==null||charge1.getTotalcharge()==null){
			return new Result(false,StatusCode.ERROR,"您没有支付，请从新支付");
		}
		//比较前台传入的支付费用，是不是一样
		if (!charge.getTotalcharge().equals(charge1.getTotalcharge())) {
			return new Result(false,StatusCode.ERROR,"请支付正确的费用");
		}
		if (data!=null){
			//一样就给charge赋值
			charge.setUserid(userId);
			charge.setUsername((String) data.get("name"));
			charge.setMobile((String) data.get("mobile"));
		}
		//调用修改的方法，修改状态为3【已支付】
		charge.setPaytime(new Date());
		charge.setState(3);
		charge.setId(id);
		chargeService.update(charge);
		return new Result(true,StatusCode.OK,"支付成功");
	}

	/**
	 * 查询当前用户有关的费用清单
	 * @return
	 */
	@RequestMapping(value = "/user/{page}/{size}",method = RequestMethod.GET)
	public Result findByUserId(@PathVariable int page, @PathVariable int size){
		//获取userid
		String userId = (String) request.getAttribute("id");
		//去查询用户的门牌id
		Map<String,Object> user = (Map<String, Object>) userClient.findById(userId).getData();

		if(user!=null){
			//根据门牌id查询所有费用清单
			Page<Charge> pagelist = chargeService.findByHoserid((String) user.get("houseid"),page,size);
			return new Result(true,StatusCode.OK,"查询成功",new PageResult<>(pagelist.getTotalElements(),pagelist.getContent()));
		}
		return new Result(false,StatusCode.ERROR,"查询失败");
	}
	
}
