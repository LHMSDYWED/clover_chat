package com.wechat.car.service;

import com.wechat.car.dao.CarDao;
import com.wechat.car.pojo.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
 * @create 2019-01-14-22:27
 * 服务层
 */
@Service
public class CarService {

	@Autowired
	private CarDao carDao;
	
	@Autowired
	private IdWorker idWorker;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Car> findAll() {
		return carDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Car> findSearch(Map whereMap, int page, int size) {
		Specification<Car> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return carDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<Car> findSearch(Map whereMap) {
		Specification<Car> specification = createSpecification(whereMap);
		return carDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Car findById(String id) {
		return carDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param car
	 */
	public void add(Car car) {
		car.setId( idWorker.nextId()+"" );
		carDao.save(car);
	}

	/**
	 * 修改
	 * @param car
	 */
	public void update(Car car) {
		carDao.save(car);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		carDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Car> createSpecification(Map searchMap) {

		return new Specification<Car>() {

			@Override
			public Predicate toPredicate(Root<Car> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // id号
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+ searchMap.get("id") +"%"));
                }
                // 车位号
                if (searchMap.get("carport")!=null && !"".equals(searchMap.get("carport"))) {
                	predicateList.add(cb.like(root.get("carport").as(String.class), "%"+ searchMap.get("carport") +"%"));
                }
                // 车牌号
                if (searchMap.get("carid")!=null && !"".equals(searchMap.get("carid"))) {
                	predicateList.add(cb.like(root.get("carid").as(String.class), "%"+ searchMap.get("carid") +"%"));
                }
                // 车主名
                if (searchMap.get("name")!=null && !"".equals(searchMap.get("name"))) {
                	predicateList.add(cb.like(root.get("name").as(String.class), "%"+ searchMap.get("name") +"%"));
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
	 * 根据手机号和车牌号查询用户
	 * @param mobile
	 * @param carid
	 * @return
	 */
	public Car findByMobileAndCarid(String mobile,String carid){
		return carDao.findByMobileAndCarid(mobile, carid);
	}
}
