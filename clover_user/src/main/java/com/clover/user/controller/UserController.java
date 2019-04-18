package com.clover.user.controller;

import com.clover.user.pojo.House;
import com.clover.user.pojo.User;
import com.clover.user.service.HouseService;
import com.clover.user.service.UserService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jiangxin
 * @create 2019-01-14-21:42
 * 小区用户的控制层
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
@Transactional
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private HouseService houseService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtUtil jwtUtil;
    /**
     * 查询全部数据
     * @return
     */
    @RequestMapping(method= RequestMethod.GET)
    public Result findAll(){
        return new Result(true,StatusCode.OK,"查询成功",userService.findAll());
    }

    /**
     * 根据ID查询
     * @param id ID
     * @return
     */
    @RequestMapping(value="/{id}",method= RequestMethod.GET)
    public Result findById(@PathVariable String id){
        return new Result(true,StatusCode.OK,"查询成功",userService.findById(id));
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
        Page<User> pageList = userService.findSearch(searchMap, page, size);
        return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<User>(pageList.getTotalElements(), pageList.getContent()) );
    }

    /**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",userService.findSearch(searchMap));
    }

    /**
     * 增加
     * @param user
     */
    @RequestMapping(method=RequestMethod.POST)
    public Result add(@RequestBody User user  ){
        userService.add(user);
        return new Result(true, StatusCode.OK,"增加成功");
    }

    /**
     * 修改
     * @param user
     */
    @RequestMapping(value="/{id}",method= RequestMethod.PUT)
    public Result update(@RequestBody User user, @PathVariable String id ){
        user.setId(id);
        userService.update(user);
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
        userService.deleteById(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 根据电话号码查询是否由此用户
     * @param mobile
     * @return
     */
    @RequestMapping(value = "/mobile/{mobile}", method = RequestMethod.POST)
    public Result findByMobile(@PathVariable String mobile){
        User user = userService.findByMobile(mobile);
        return new Result(true,StatusCode.OK,"查询成功",user);
    }
    /**
     * 发送短信
     * @param mobile
     * @return
     */
    @RequestMapping(value = "/sendsms/{mobile}", method = RequestMethod.POST)
    public Result sendJuhe(@PathVariable String mobile){
        userService.sendJuhe(mobile);
        return new Result(true,StatusCode.OK,"发送成功，请注意接收");
    }

    /**
     * 用户填写验证码后的注册
     * @param code
     * @param user
     * @return
     */
    @RequestMapping(value = "/register/{code}", method = RequestMethod.POST)
    public Result regist(@PathVariable String code, @RequestBody User user){
        User user1 = userService.findByMobile(user.getMobile());
        if (user1!=null){
            return new Result(false, StatusCode.REPERROR, "您已经注册过了");
        }
        //得到缓存中的验证码
        String checkcodeRedis = (String) redisTemplate.opsForValue().get("checkcode_" + user.getMobile());
        if(checkcodeRedis.isEmpty()){
            return new Result(false, StatusCode.ERROR, "请先获取手机验证码");
        }
        if(!checkcodeRedis.equals(code)){
            return new Result(false, StatusCode.ERROR, "请输入正确的验证码");
        }
        //判断是否为小区用户
        House house = houseService.findByMobile(user.getMobile());
        //为houseid赋值
        user.setHouseid(house.getId());
        if (house==null){
            return new Result(false, StatusCode.ERROR, "您还不是小区用户，或没有在物业处登记，请登记后来试");
        }
        userService.add(user);
        return new Result(true, StatusCode.OK, "注册成功");
    }

    /**
     * 微信用户注册 根据id 修改内容
     * @param code  验证码
     * @param user  token 、mobile、name、carid、openid、nickname,avatar,sex
     * @return
     */
    @RequestMapping(value = "/register/{code}", method = RequestMethod.PUT)
    public Result weChatRegister(@PathVariable String code, @RequestBody User user){
        //先判断是不是微信用户【临时用户】
        String token = (String) request.getAttribute("claims_visitor");
        if (token==null || "".equals(token)){
            String token1 = (String) request.getAttribute("claims_user");
            if(token1==null || "".equals(token)){
                return new Result(false, StatusCode.ACCESSERROR,"权限不足");
            }
        }
        //得到缓存中的验证码【判断验证码是否正确】
        String checkcodeRedis = (String) redisTemplate.opsForValue().get("checkcode_" + user.getMobile());
        if(checkcodeRedis.isEmpty()){
            return new Result(false, StatusCode.ERROR, "请先获取手机验证码");
        }
        if(!checkcodeRedis.equals(code)){
            return new Result(false, StatusCode.ERROR, "请输入正确的验证码");
        }
        //判断是否为小区用户
        House house = houseService.findByMobile(user.getMobile());
        if (house==null){
            return new Result(false, StatusCode.ERROR, "您还不是小区用户，或没有在物业处登记，请登记后来试");
        }
        //为houseid赋值
        user.setHouseid(house.getId());
        String id = (String) request.getAttribute("id"); //得到用户的id
        System.out.println(id);
        user.setId(id);
        user.setState(2);
        user.setRegdate(new Date());
        userService.update(user);
        String token1 = jwtUtil.createJWT(user.getId(), user.getHouseid(), "user");
        Map<String, Object> map = new HashMap<>();
        map.put("token", token1);
        map.put("roles", "user");
        return new Result(true, StatusCode.OK, "恭喜成为小区的一员",map);
    }



    /**
     * 密码登录
     * @param user
     * @return
     */
    @RequestMapping(value = "/loginpwd", method = RequestMethod.POST)
    public Result loginPwd(@RequestBody User user){
        user = userService.loginPwd(user.getMobile(), user.getPassword());
        if(user==null){
            return new Result(false, StatusCode.LOGINERROR, "登录失败");
        }
        String token = jwtUtil.createJWT(user.getId(), user.getHouseid(), "user");
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("roles", "user");
        return new Result(true, StatusCode.OK, "登录成功", map);
    }

    /**
     * 验证码登录
     * @param code
     * @param user
     * @return
     */
    @RequestMapping(value = "/login/{code}", method = RequestMethod.POST)
    public Result login(@PathVariable String code,@RequestBody User user){
        User user1 = userService.findByMobile(user.getMobile());
        if(user1==null){
            return new Result(false, StatusCode.LOGINERROR, "您还没有注册为小区用户");
        }
        //得到缓存中的验证码
        String checkcodeRedis = (String) redisTemplate.opsForValue().get("checkcode_" + user.getMobile());
        if(checkcodeRedis.isEmpty()){
            return new Result(false, StatusCode.ERROR, "请先获取手机验证码");
        }
        if(!checkcodeRedis.equals(code)){
            return new Result(false, StatusCode.ERROR, "请输入正确的验证码");
        }
        String token = jwtUtil.createJWT(user1.getId(), user1.getHouseid(), "user");
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("roles", "user");
        return new Result(true, StatusCode.OK, "登录成功", map);
    }

    /**
     * 验证短信验证码的方法(忘记密码时，短信验证修改)
     * @param code
     * @param user
     * @return
     */
    @RequestMapping(value = "/check/{code}", method = RequestMethod.POST)
    public Result checkCode(@PathVariable String code,@RequestBody User user){
        //先验证有没有这个人
        User user1 = userService.findByMobile(user.getMobile());
        if (user1==null){
            return new Result(false, StatusCode.REPERROR, "您并未注册");
        }
        //得到缓存中的验证码
        String checkcodeRedis = (String) redisTemplate.opsForValue().get("checkcode_" + user.getMobile());
        if(checkcodeRedis.isEmpty()){
            return new Result(false, StatusCode.ERROR, "请先获取手机验证码");
        }
        if(!checkcodeRedis.equals(code)){
            return new Result(false, StatusCode.ERROR, "请输入正确的验证码");
        }
        return new Result(true,StatusCode.OK,"验证码输入正确",user1);
    }

    /**
     * 根据openid查询用户户
     * @param openid
     * @return
     */
    @RequestMapping(value = "/openid/{openid}",method = RequestMethod.GET)
    public Result findByOpenid(@PathVariable String openid){
        return new Result(true,StatusCode.OK,"查询成功",userService.findByOpenid(openid));
    }

    /**
     * 微信授权登录
     * @param user
     * @return
     */
    @RequestMapping(value = "/wechatlogin",method = RequestMethod.POST)
    public Result weChatLogin(@RequestBody User user){
        if(user.getOpenid()==null||"".equals(user.getOpenid())){
            return new Result(false,StatusCode.ERROR,"登录失败");
        }
        System.out.println(user.getOpenid());
        System.out.println(user.getNickname());
        //先判断有没有此人，根据openid
        User user1 = userService.findByOpenid(user.getOpenid());
        if (user1==null){
            //没有就注册一个，并给一个临时token
            user.setState(1);
            userService.addWeChat(user);
            User user2 = userService.findByOpenid(user.getOpenid());
            String token = jwtUtil.createJWT(user2.getId(), user2.getOpenid(), "visitor");
            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
            map.put("roles", "visitor");
            return new Result(true,StatusCode.OK,"登录成功",map);
        }
        //有就根据状态判断是临时还是小区用户
        if (user1.getState()==null||user1.getState()==1){
            //临时就给游客token
            String token = jwtUtil.createJWT(user1.getId(), user1.getOpenid(), "visitor");
            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
            map.put("roles", "visitor");
            return new Result(true,StatusCode.OK,"登录成功",map);
        }
        //小区就给用户token
        String token = jwtUtil.createJWT(user1.getId(), user1.getHouseid(), "user");
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("roles", "user");
        return new Result(true, StatusCode.OK, "登录成功", map);
    }

}
