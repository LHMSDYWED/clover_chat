package com.wechat.car.controller;
import com.wechat.car.client.UserClient;
import com.wechat.car.pojo.Car;
import com.wechat.car.service.CarService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import javafx.scene.input.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
/**
 * @author jiangxin
 * @create 2019-01-14-22:37
 * 控制层
 */
@RestController
@CrossOrigin
@RequestMapping("/car")
public class CarController {

	@Autowired
	private CarService carService;

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
		return new Result(true,StatusCode.OK,"查询成功",carService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",carService.findById(id));
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
		Page<Car> pageList = carService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Car>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",carService.findSearch(searchMap));
    }
	
	/**
	 * 增加/前端只增加车位号，需要管理员权限
	 * @param car
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Car car  ){
		//必须是admin角色才可以增加
		String token = (String) request.getAttribute("claims_admin");
		if (token==null || "".equals(token)){
			return new Result(false, StatusCode.ACCESSERROR,"权限不足");
		}
		carService.add(car);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param car
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Car car, @PathVariable String id ){
		car.setId(id);
		carService.update(car);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		carService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 增加小区车位的用户信息，就是修改车位表里的信息【需要管理员权限】
	 * 前台一定要传入车位编号和车位ID号
	 * @param id 车位ID号
	 * @param car   前台传入的车位用户的信息【是购买还是租用，由前台下拉框选择、时间也一样】
	 * @return
	 */
	@RequestMapping(value="/usercar/{id}",method= RequestMethod.PUT)
	public Result addUserCar(@PathVariable String id,@RequestBody Car car){
		//必须是admin角色才可以增加
		String token = (String) request.getAttribute("claims_admin");
		if (token==null || "".equals(token)){
			return new Result(false, StatusCode.ACCESSERROR,"权限不足");
		}
		//必须是小区用户才可以购买
		Result result = userClient.findByMobile(car.getMobile());
		if (result.getData()==null) {
			return new Result(false, StatusCode.ERROR, "您不是该小区的用户");
		}
		Car car1 = carService.findById(id);
		if (car1==null){
			return new Result(false,StatusCode.ERROR,"没有该车位");
		}
		if (car1.getState()!=2||car1.getState()!=null){
			return new Result(false,StatusCode.ERROR,"该车位正在使用或有故障");
		}
		car.setId(id);
		car.setState(1);  //讲使用状态变为正在使用
		car.setStarttime(new Date()); //开始时间，在这里就是购买或者租用的起始时间
		carService.update(car);
		if (car.getType().equals(1)){
			return new Result(true,StatusCode.OK,"购买车位成功");
		}
		return new Result(true,StatusCode.OK,"租用车位成功");
	}

	/**
	 * 车位临时用户进入车位
	 * @param id  车位ID号
	 * @param car  传入临时用户的信息
	 *             name,mobile，carid,
	 * @return
	 */
	@RequestMapping(value="/visitorcar/{id}",method= RequestMethod.PUT)
	public Result addVisitorCar(@PathVariable String id,@RequestBody Car car){
		Car car1 = carService.findById(id);
		if (car1==null){
			return new Result(false,StatusCode.ERROR,"没有该车位");
		}
		if (car1.getState()!=2||car1.getState()!=null){
			return new Result(false,StatusCode.ERROR,"该车位正在使用或有故障");
		}
		car.setId(id);
		car.setType(3);
		car.setStarttime(new Date());
		car.setState(1);
		car.setUnitprice(3.00);
		car.setState(1);
		carService.update(car);
		return new Result(true,StatusCode.OK,"临时停车成功");
	}

	/**
	 * 临时用户离开车位，这里其实就是查询信息而已
	 * @param id 车位ID号
	 * @param car 传入临时用户的信息
	 *            name,mobile，carid,
	 * @return
	 */
	@RequestMapping(value="/leavevisitor/{id}",method= RequestMethod.GET)
	public Result leaveVisitorCar(@PathVariable String id,@RequestBody Car car){
		//先查询需要好多费用，以及临时停车的时间
		Car visitorCar = carService.findByMobileAndCarid(car.getMobile(), car.getCarid());
		if (visitorCar==null){
			return new Result(false,StatusCode.ERROR,"请输入正确的车牌号和手机号");
		}
		visitorCar.setLeavetime(new Date());//设置离开时间
		//计算停车时长
		long time = visitorCar.getLeavetime().getTime() - visitorCar.getStarttime().getTime(); //毫秒数
		//计算小时
		long hour = time/(3600*1000);
		//计算分钟
		long minute = time/(60*1000);
		//计算秒数,可以不用
		long second=time/1000;
		if (minute>15){
			visitorCar.setPay((hour+1)*visitorCar.getUnitprice());
		}else {
			visitorCar.setPay(hour*visitorCar.getUnitprice());
		}
		System.out.println(visitorCar.getPay());
		System.out.println(time);
		//讲结果返回给前台，前台付费成功后，跳转到付费成功的方法
		return new Result(true,StatusCode.OK,"请支付费用",visitorCar);
	}

	@RequestMapping(value="/payprice/{id}",method= RequestMethod.PUT)
	public Result payPrice(@PathVariable String id,@RequestBody Car car){
		car.setId(id);
		car.setState(2);
		carService.update(car);
		return new Result(false,StatusCode.OK,"支付成功");
	}

//	/**
//	 * 增加租用车位的用户信息，就是修改车位表里的信息【需要管理员权限】
//	 * 前台一定要传入车位编号和车位ID号
//	 * @param id  车位ID号
//	 * @param car  前台传入的租用车位用户的信息
//	 * @return
//	 */
//	@RequestMapping(value="/rentcar/{id}",method= RequestMethod.PUT)
//	public Result addRentCar(@PathVariable String id,@RequestBody Car car){
//		//必须是admin角色才可以增加
//		String token = (String) request.getAttribute("claims_admin");
//		if (token==null || "".equals(token)){
//			return new Result(false, StatusCode.ACCESSERROR,"权限不足");
//		}
//		return new Result(false,StatusCode.OK,"租用成功");
//	}



}
