package com.wechat.dynamic.service;

import com.wechat.dynamic.dao.DynamicDao;
import com.wechat.dynamic.pojo.Dynamic;
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
 * @create 2019-01-14-23:12
 * 服务层
 */
@Service
public class DynamicService {

	@Autowired
	private DynamicDao dynamicDao;
	
	@Autowired
	private IdWorker idWorker;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Dynamic> findAll() {
		return dynamicDao.findAll();
	}
	/**
	 * 查询所有并分页
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Dynamic> findAllPage(int page, int size) {
		List<Sort.Order> orders=new ArrayList<Sort.Order>();
		orders.add( new Sort.Order(Sort.Direction. ASC, "istop"));
		orders.add( new Sort.Order(Sort.Direction. ASC, "state"));
		orders.add( new Sort.Order(Sort.Direction. DESC, "createtime"));
		Pageable pageable = PageRequest.of(page-1,size,new Sort(orders));
		return dynamicDao.findAll(pageable);
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Dynamic> findSearch(Map whereMap, int page, int size) {
		Specification<Dynamic> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return dynamicDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<Dynamic> findSearch(Map whereMap) {
		Specification<Dynamic> specification = createSpecification(whereMap);
		return dynamicDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Dynamic findById(String id) {
		return dynamicDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param dynamic
	 */
	public void add(Dynamic dynamic) {
		dynamic.setId( idWorker.nextId()+"" );
		dynamicDao.save(dynamic);
	}

	/**
	 * 修改
	 * @param dynamic
	 */
	public void update(Dynamic dynamic) {
		dynamicDao.save(dynamic);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		dynamicDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Dynamic> createSpecification(Map searchMap) {

		return new Specification<Dynamic>() {

			@Override
			public Predicate toPredicate(Root<Dynamic> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID号
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+ searchMap.get("id") +"%"));
                }
                // 主题
                if (searchMap.get("theme")!=null && !"".equals(searchMap.get("theme"))) {
                	predicateList.add(cb.like(root.get("theme").as(String.class), "%"+ searchMap.get("theme") +"%"));
                }
                // 类容
                if (searchMap.get("content")!=null && !"".equals(searchMap.get("content"))) {
                	predicateList.add(cb.like(root.get("content").as(String.class), "%"+ searchMap.get("content") +"%"));
                }
                // 图片
                if (searchMap.get("images")!=null && !"".equals(searchMap.get("images"))) {
                	predicateList.add(cb.like(root.get("images").as(String.class), "%"+ searchMap.get("images") +"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

	/**
	 * 增加点赞数
	 * @param id
	 */
	public void addThumbup(String id){
		dynamicDao.addThumbup(id);
	}

	/**
	 * 减少点赞数
	 * @param id
	 */
	public void minusThumbup(String id){
		dynamicDao.minusThumbup(id);
	}

	/**
	 * 增加评论数
	 * @param id
	 */
	public void addComment(String id){
		dynamicDao.addComment(id);
	}

	/**
	 * 减少评论数
	 * @param id
	 */
	public void minusComment(String id){
		dynamicDao.minusComment(id);
	}
}
