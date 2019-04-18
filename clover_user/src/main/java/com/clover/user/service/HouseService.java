package com.clover.user.service;

import com.clover.user.dao.HouseDao;
import com.clover.user.pojo.House;
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
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author jiangxin
 * @create 2019-01-14-21:58
 * 服务层
 */
@Service
public class HouseService {
    @Autowired
    private HouseDao houseDao;

    @Autowired
    private IdWorker idWorker;



    /**
     * 查询全部列表
     * @return
     */
    public List<House> findAll() {
        return houseDao.findAll();
    }


    /**
     * 条件查询+分页
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<House> findSearch(Map whereMap, int page, int size) {
        Specification<House> specification = createSpecification(whereMap);
        PageRequest pageRequest =  PageRequest.of(page-1, size);
        return houseDao.findAll(specification, pageRequest);
    }


    /**
     * 条件查询
     * @param whereMap
     * @return
     */
    public List<House> findSearch(Map whereMap) {
        Specification<House> specification = createSpecification(whereMap);
        return houseDao.findAll(specification);
    }

    /**
     * 根据ID查询实体
     * @param id
     * @return
     */
    public House findById(String id) {
        Optional<House> house = houseDao.findById(id);
        if(house!=null&&house.isPresent()){
            return houseDao.findById(id).get();
        }
        return null;
    }

    /**
     * 增加
     * @param house
     */
    public void add(House house) {
        house.setId( idWorker.nextId()+"" );
        houseDao.save(house);
    }

    /**
     * 修改
     * @param house
     */
    public void update(House house) {
        houseDao.save(house);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteById(String id) {
        houseDao.deleteById(id);
    }

    /**
     * 动态条件构建
     * @param searchMap
     * @return
     */
    private Specification<House> createSpecification(Map searchMap) {

        return new Specification<House>() {

            @Override
            public Predicate toPredicate(Root<House> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // 门牌ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%"+ searchMap.get("id") +"%"));
                }
                // 门牌号
                if (searchMap.get("housenumber")!=null && !"".equals(searchMap.get("housenumber"))) {
                    predicateList.add(cb.like(root.get("housenumber").as(String.class), "%"+ searchMap.get("housenumber") +"%"));
                }
                // 手机号
                if (searchMap.get("mobile")!=null && !"".equals(searchMap.get("mobile"))) {
                    predicateList.add(cb.like(root.get("mobile").as(String.class), "%"+ searchMap.get("mobile") +"%"));
                }
                // 姓名
                if (searchMap.get("name")!=null && !"".equals(searchMap.get("name"))) {
                    predicateList.add(cb.like(root.get("name").as(String.class), "%"+ searchMap.get("name") +"%"));
                }
                // 身份证号
                if (searchMap.get("idcard")!=null && !"".equals(searchMap.get("idcard"))) {
                    predicateList.add(cb.like(root.get("idcard").as(String.class), "%"+ searchMap.get("idcard") +"%"));
                }

                return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }

    /**
     * 通过手机号查询是否为小区用户
     * @param mobile
     * @return
     */
    public House findByMobile(String mobile){
        return  houseDao.findByMobile(mobile);
    }

    /**
     * 根据门牌号查询
     * @param housenumber
     * @return
     */
    public House findByHousenumber(String housenumber){
        return houseDao.findByHousenumber(housenumber);
    }
}
