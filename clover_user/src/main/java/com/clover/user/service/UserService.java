package com.clover.user.service;

import com.clover.user.dao.UserDao;
import com.clover.user.pojo.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author jiangxin
 * @create 2019-01-14-21:53
 * 服务层
 */
@Service
@Transactional
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BCryptPasswordEncoder encoder;
    /**
     * 查询全部列表
     * @return
     */
    public List<User> findAll() {
        return userDao.findAll();
    }


    /**
     * 条件查询+分页
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<User> findSearch(Map whereMap, int page, int size) {
        Specification<User> specification = createSpecification(whereMap);
        PageRequest pageRequest =  PageRequest.of(page-1, size);
        return userDao.findAll(specification, pageRequest);
    }


    /**
     * 条件查询
     * @param whereMap
     * @return
     */
    public List<User> findSearch(Map whereMap) {
        Specification<User> specification = createSpecification(whereMap);
        return userDao.findAll(specification);
    }

    /**
     * 根据ID查询实体
     * @param id
     * @return
     */
    public User findById(String id) {
        return userDao.findById(id).get();
    }

    /**
     * 增加数据的方法
     * @param user
     */
    public void add(User user) {
//        user.setId( idWorker.nextId()+"" );
//        user.setRegdate(new Date());
        user.setUpdatedate(new Date());
        user.setLastdate(new Date());
        //密码加密
        user.setPassword(encoder.encode(user.getPassword()));
        userDao.save(user);
    }

    /**
     * 修改
     * @param user
     */
    public void update(User user) {
        //密码加密
        if (user.getPassword()!=null){
            user.setPassword(encoder.encode(user.getPassword()));
        }
        user.setUpdatedate(new Date());
        user.setLastdate(new Date());
        userDao.save(user);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteById(String id) {
        userDao.deleteById(id);
    }

    /**
     * 根据手机号码查询是否有此人
     * @param mobile
     */
    public User findByMobile(String mobile){
        User user = userDao.findByMobile(mobile);
        System.out.println(user);
        return user;
    }

    /**
     * 动态条件构建
     * @param searchMap
     * @return
     */
    private Specification<User> createSpecification(Map searchMap) {

        return new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // id号
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%"+ searchMap.get("id") +"%"));
                }
                // 微信id
                if (searchMap.get("wechatid")!=null && !"".equals(searchMap.get("wechatid"))) {
                    predicateList.add(cb.like(root.get("wechatid").as(String.class), "%"+ searchMap.get("wechatid") +"%"));
                }
                // 手机号
                if (searchMap.get("mobile")!=null && !"".equals(searchMap.get("mobile"))) {
                    predicateList.add(cb.like(root.get("mobile").as(String.class), "%"+ searchMap.get("mobile") +"%"));
                }
                // 微信昵称
                if (searchMap.get("nickname")!=null && !"".equals(searchMap.get("nickname"))) {
                    predicateList.add(cb.like(root.get("nickname").as(String.class), "%"+ searchMap.get("nickname") +"%"));
                }
                // 头像
                if (searchMap.get("avatar")!=null && !"".equals(searchMap.get("avatar"))) {
                    predicateList.add(cb.like(root.get("avatar").as(String.class), "%"+ searchMap.get("avatar") +"%"));
                }
                // 车牌号
                if (searchMap.get("carid")!=null && !"".equals(searchMap.get("carid"))) {
                    predicateList.add(cb.like(root.get("carid").as(String.class), "%"+ searchMap.get("carid") +"%"));
                }
                // 房间号
                if (searchMap.get("houseid")!=null && !"".equals(searchMap.get("houseid"))) {
                    predicateList.add(cb.like(root.get("houseid").as(String.class), "%"+ searchMap.get("houseid") +"%"));
                }

                return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }

    /**
     * 短信发送的方法
     * @param mobile
     */
    public void sendJuhe(String mobile){
        //生成六位数字随机数
        String checkcode = RandomStringUtils.randomNumeric(6);
        //向缓存中放一份,失效五分钟【测试时是30天】
        redisTemplate.opsForValue().set("checkcode_"+mobile, checkcode, 30, TimeUnit.DAYS);
        //给用户发一份
        Map<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("checkcode", checkcode);
        rabbitTemplate.convertAndSend("juhe", map);
        //在控制台显示一份【方便测试】
        System.out.println("验证码为："+checkcode);
    }

    /**
     * 密码登录
     * @param mobile
     * @param password
     * @return
     */
    public User loginPwd(String mobile, String password) {
            User user = userDao.findByMobile(mobile);
            if(user!=null && encoder.matches(password, user.getPassword())){
                return user;
            }
            return null;
        }

    /**
     * 验证码登录
     * @param mobile
     * @return
     */
    public User login(String mobile) {
        return userDao.findByMobile(mobile);
    }

    /**
     * 根据微信号唯一编码查询用户实体
     * @param openid
     * @return
     */
    public User findByOpenid(String openid){
        return userDao.findByOpenid(openid);
    }
    public void addWeChat(User user) {
        user.setId( idWorker.nextId()+"" );
        user.setRegdate(new Date());
        user.setUpdatedate(new Date());
        user.setLastdate(new Date());
        userDao.save(user);
    }
}
