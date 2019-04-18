package com.wechat.charge.service;

import com.wechat.charge.dao.ChargeDao;
import com.wechat.charge.pojo.Charge;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author jiangxin
 * @create 2019-01-14-22:49
 * 服务层
 */
@Service
public class ChargeService {

	@Autowired
	private ChargeDao chargeDao;
	
	@Autowired
	private IdWorker idWorker;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Charge> findAll() {
		return chargeDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Charge> findSearch(Map whereMap, int page, int size) {
		Specification<Charge> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return chargeDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<Charge> findSearch(Map whereMap) {
		Specification<Charge> specification = createSpecification(whereMap);
		return chargeDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Charge findById(String id) {
		return chargeDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param charge
	 */
	public void add(Charge charge) {
		charge.setId( idWorker.nextId()+"" );
		charge.setGenerationtime(new Date());
		charge.setState(1);
		chargeDao.save(charge);
	}

	/**
	 * 修改
	 * @param charge
	 */
	public void update(Charge charge) {
		chargeDao.save(charge);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		chargeDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Charge> createSpecification(Map searchMap) {

		return new Specification<Charge>() {

			@Override
			public Predicate toPredicate(Root<Charge> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // id号
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+ searchMap.get("id") +"%"));
                }
                // 门牌ID
                if (searchMap.get("hoserid")!=null && !"".equals(searchMap.get("hoserid"))) {
                	predicateList.add(cb.like(root.get("hoserid").as(String.class), "%"+ searchMap.get("hoserid") +"%"));
                }
                // 门牌号
                if (searchMap.get("housenumber")!=null && !"".equals(searchMap.get("housenumber"))) {
                	predicateList.add(cb.like(root.get("housenumber").as(String.class), "%"+ searchMap.get("housenumber") +"%"));
                }
                // 支付人ID
                if (searchMap.get("userid")!=null && !"".equals(searchMap.get("userid"))) {
                	predicateList.add(cb.like(root.get("userid").as(String.class), "%"+ searchMap.get("userid") +"%"));
                }
                // 支付人姓名
                if (searchMap.get("username")!=null && !"".equals(searchMap.get("username"))) {
                	predicateList.add(cb.like(root.get("username").as(String.class), "%"+ searchMap.get("username") +"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

	/**
	 * 根据门牌id查询所有 按时间,和状态排序
	 * @param houseid
	 * @return
	 */
    public Page<Charge> findByHoserid(String houseid,int page, int size) {
		List<Sort.Order> orders=new ArrayList<Sort.Order>();
		orders.add( new Sort.Order(Sort.Direction. ASC, "state"));
		orders.add( new Sort.Order(Sort.Direction. DESC, "generationtime"));
		Pageable pageable = PageRequest.of(page-1,size,new Sort(orders));
		return chargeDao.findByHoserid(houseid,pageable);
    }
}
