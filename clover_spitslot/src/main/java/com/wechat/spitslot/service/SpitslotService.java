package com.wechat.spitslot.service;

import com.wechat.spitslot.dao.SpitslotDao;
import com.wechat.spitslot.pojo.Spitslot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author jiangxin
 * @create 2019-01-15-00:12
 * 服务层
 */
@Service
public class SpitslotService {

	@Autowired
	private SpitslotDao spitslotDao;
	
	@Autowired
	private IdWorker idWorker;

	@Autowired
	private MongoTemplate mongoTemplate; //我们可以使用MongoTemplate类来实现对某列的操作,如增加，减少

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Spitslot> findAll() {
		return spitslotDao.findAll();
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Spitslot findById(String id) {
		return spitslotDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param spitslot
	 */
	public void add(Spitslot spitslot) {
		spitslot.set_id(idWorker.nextId()+"" );
		spitslot.setPublishtime(new Date());//发布日期
		spitslot.setVistis(0); //浏览量
		spitslot.setThumbup(0);//点赞数
		spitslot.setComment(0);//回复数
		spitslot.setState("1");//状态
		//如果存在上级ID,评论 ,则回复数要加一
		if(spitslot.getParentid()!=null&!"".equals(spitslot.getParentid())){
			Query query = new Query();
			query.addCriteria(Criteria.where("_id").is(spitslot.getParentid()));
			Update update = new Update();
			update.inc("comment",1);
			mongoTemplate.updateFirst(query,update,"spitslot");
		}
		spitslotDao.save(spitslot);
	}

	/**
	 * 修改
	 * @param spitslot
	 */
	public void update(Spitslot spitslot) {
		spitslotDao.save(spitslot);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		spitslotDao.deleteById(id);
	}

	/**
	 * 根据上级ID查询吐槽列表
	 * @param parentid
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Spitslot> findByParentid(String parentid, int page, int size){
		List<Sort.Order> orders=new ArrayList<Sort.Order>();
		orders.add( new Sort.Order(Sort.Direction. DESC, "publishtime"));
		orders.add( new Sort.Order(Sort.Direction. DESC, "thumbup"));
		PageRequest pageRequest = PageRequest.of(page-1, size,new Sort(orders));
		return spitslotDao.findAllByParentid(parentid,pageRequest);
	}

	/**
	 * 点赞
	 * @param id
	 */
	public void addthumbup(String id) {
		Query query = new Query(); //条件
		query.addCriteria(Criteria.where("_id").is(id));
		Update update = new Update();//要改变的字段
		update.inc("thumbup",1);//加1
		mongoTemplate.updateFirst(query,update,"spitslot");
	}

	/**
	 * 取消赞
	 * @param id
	 */
	public void minusthumbup(String id) {
		Query query = new Query(); //条件
		query.addCriteria(Criteria.where("_id").is(id));
		Update update = new Update();//要改变的字段
		update.inc("thumbup",-1);//减1
		mongoTemplate.updateFirst(query,update,"spitslot");
	}

	/**
	 * 查询所有并分页
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Spitslot> querryAll(int page, int size){
		List<Sort.Order> orders=new ArrayList<Sort.Order>();
		orders.add( new Sort.Order(Sort.Direction. DESC, "publishtime"));
		orders.add( new Sort.Order(Sort.Direction. DESC, "thumbup"));
		Pageable pageable = PageRequest.of(page-1,size,new Sort(orders));
		return spitslotDao.findAllByParentidIsNull(pageable);
	}
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Spitslot> findSearch(Map whereMap, int page, int size) {
		List<Sort.Order> orders=new ArrayList<Sort.Order>();
		orders.add( new Sort.Order(Sort.Direction. DESC, "publishtime"));
		orders.add( new Sort.Order(Sort.Direction. DESC, "thumbup"));
		Pageable pageable = PageRequest.of(page-1,size,new Sort(orders));
		Query query = new Query(); //条件
		Pattern pattern=Pattern.compile("^.*"+whereMap.get("querryMap")+".*$", Pattern.CASE_INSENSITIVE);
		Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("content").regex(pattern),
				Criteria.where("parentid").is(null));
//				Criteria.where("nickname").regex(pattern));
		query.addCriteria(criteria);

//		query.addCriteria(Criteria.where("content").regex((String) whereMap.get("map")));
////		query.addCriteria(Criteria.where("parentid").is(""));
//		query.addCriteria(Criteria.where("nickname").regex((String) whereMap.get("map")));
		List<Spitslot> spitslot = mongoTemplate.find(query.with(pageable), Spitslot.class, "spitslot");
		return new PageImpl(spitslot, pageable, spitslot.size());
	}

	/**
	 * 根据userid查询并分页
	 * @param userid
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Spitslot> findByUserid(String userid, int page, int size){
		List<Sort.Order> orders=new ArrayList<Sort.Order>();
		orders.add( new Sort.Order(Sort.Direction. DESC, "publishtime"));
		orders.add( new Sort.Order(Sort.Direction. DESC, "thumbup"));
		PageRequest pageRequest = PageRequest.of(page-1, size,new Sort(orders));
		return spitslotDao.findAllByUseridAndParentidIsNull(userid,pageRequest);
	}

}
