package com.wechat.repair.service;

import com.wechat.repair.dao.RepairDao;
import com.wechat.repair.pojo.Repair;
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
 * @create 2019-01-14-23:52
 * 服务层
 */
@Service
public class RepairService {

	@Autowired
	private RepairDao repairDao;
	
	@Autowired
	private IdWorker idWorker;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Repair> findAll() {
		return repairDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Repair> findSearch(Map whereMap, int page, int size) {
		Specification<Repair> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return repairDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<Repair> findSearch(Map whereMap) {
		Specification<Repair> specification = createSpecification(whereMap);
		return repairDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Repair findById(String id) {
		return repairDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param repair
	 */
	public void add(Repair repair) {
		repair.setId( idWorker.nextId()+"" );
		repairDao.save(repair);
	}

	/**
	 * 修改
	 * @param repair
	 */
	public void update(Repair repair) {
		repairDao.save(repair);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		repairDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Repair> createSpecification(Map searchMap) {

		return new Specification<Repair>() {

			@Override
			public Predicate toPredicate(Root<Repair> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID号
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
                // 用户ID
                if (searchMap.get("userid")!=null && !"".equals(searchMap.get("userid"))) {
                	predicateList.add(cb.like(root.get("userid").as(String.class), "%"+ searchMap.get("userid") +"%"));
                }
                // 用户ID
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
	 * 根据用户id查询保修列表，并分页
	 * @param userid
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Repair> findByUserid(String userid, int page, int size){
		List<Sort.Order> orders=new ArrayList<Sort.Order>();
		orders.add( new Sort.Order(Sort.Direction. ASC, "state"));
		orders.add( new Sort.Order(Sort.Direction. DESC, "repairtime"));
		Pageable pageable = PageRequest.of(page-1,size,new Sort(orders));
		return repairDao.findByUserid(userid,pageable);
	}

	/**
	 * 根据用户ID和主键ID查询保修的实体
	 * @param userid
	 * @param id
	 * @return
	 */
	public Repair findByUseridAndId(String userid,String id){
		return repairDao.findByUseridAndId(userid,id);
	}

	/**
	 * 根据用户ID和主键ID删除保修的实体
	 * @param userid
	 * @param id
	 */
	public void deleteByUseridAndId(String userid,String id){
		repairDao.deleteByUseridAndId(userid,id);
	}

	/**
	 * 根据用户id查询此用户所有报修内容
	 * @param userid
	 * @return
	 */
	public List<Repair> queryByUserid(String userid) {
		return repairDao.findByUserid(userid);
	}
}
