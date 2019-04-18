package com.wechat.feedback.service;

import com.wechat.feedback.dao.FeedbackDao;
import com.wechat.feedback.pojo.Feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author jiangxin
 * @create 2019-01-14-23:32
 * 服务层
 */
@Service
public class FeedbackService {

	@Autowired
	private FeedbackDao feedbackDao;
	
	@Autowired
	private IdWorker idWorker;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Feedback> findAll() {
		return feedbackDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Feedback> findSearch(Map whereMap, int page, int size) {
		Specification<Feedback> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return feedbackDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<Feedback> findSearch(Map whereMap) {
		Specification<Feedback> specification = createSpecification(whereMap);
		return feedbackDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Feedback findById(String id) {
		return feedbackDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param feedback
	 */
	public void add(Feedback feedback) {
		feedback.setId( idWorker.nextId()+"" );
		feedbackDao.save(feedback);
	}

	/**
	 * 修改
	 * @param feedback
	 */
	public void update(Feedback feedback) {
		feedbackDao.save(feedback);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		feedbackDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Feedback> createSpecification(Map searchMap) {

		return new Specification<Feedback>() {

			@Override
			public Predicate toPredicate(Root<Feedback> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // id号
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+ searchMap.get("id") +"%"));
                }
                // 内容
                if (searchMap.get("content")!=null && !"".equals(searchMap.get("content"))) {
                	predicateList.add(cb.like(root.get("content").as(String.class), "%"+ searchMap.get("content") +"%"));
                }
                // 图片
                if (searchMap.get("images")!=null && !"".equals(searchMap.get("images"))) {
                	predicateList.add(cb.like(root.get("images").as(String.class), "%"+ searchMap.get("images") +"%"));
                }
                // 反馈人ID
                if (searchMap.get("userid")!=null && !"".equals(searchMap.get("userid"))) {
                	predicateList.add(cb.like(root.get("userid").as(String.class), "%"+ searchMap.get("userid") +"%"));
                }
                // 反馈人姓名
                if (searchMap.get("username")!=null && !"".equals(searchMap.get("username"))) {
                	predicateList.add(cb.like(root.get("username").as(String.class), "%"+ searchMap.get("username") +"%"));
                }
                // 手机号
                if (searchMap.get("mobile")!=null && !"".equals(searchMap.get("mobile"))) {
                	predicateList.add(cb.like(root.get("mobile").as(String.class), "%"+ searchMap.get("mobile") +"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

	/**
	 * 根据用户id查询反馈列表，并分页
	 * @param userid
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Feedback> findByUserid(String userid,int page, int size){
		List<Sort.Order> orders=new ArrayList<Sort.Order>();
//		orders.add( new Sort.Order(Sort.Direction. ASC, "type"));
		orders.add( new Sort.Order(Sort.Direction. DESC, "backtime"));
		orders.add( new Sort.Order(Sort.Direction. DESC, "state"));
		Pageable pageable = PageRequest.of(page-1,size,new Sort(orders));
		return feedbackDao.findByUserid(userid, pageable);
	}

	/**
	 * 根据用户ID和主键ID查询反馈的实体
	 * @param userid
	 * @param id
	 * @return
	 */
	public Feedback findByUseridAndId(String userid,String id){
		return feedbackDao.findByUseridAndId(userid,id);
	}

	/**
	 * 根据用户ID和主键ID删除反馈的实体
	 * @param userid
	 * @param id
	 */
	public void deleteByUseridAndId(String userid,String id){
		feedbackDao.deleteByUseridAndId(userid,id);
	}

}
