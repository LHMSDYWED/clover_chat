package com.wechat.spitslot.dao;


import com.wechat.spitslot.pojo.Spitslot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author jiangxin
 * @create 2019-01-15-00:10
 * 吐槽信息表的数据访问接口
 */
public interface SpitslotDao extends MongoRepository<Spitslot, String> {
    /**
     * 根据上级id查询吐槽列表（分页）
     * @param parentid
     * @param pageable
     * @return
     */
    public Page<Spitslot> findAllByParentid(String parentid, Pageable pageable);

    /**
     * 查询没有父id的信息
     * @param pageable
     * @return
     */
    public Page<Spitslot> findAllByParentidIsNull(Pageable pageable);

    /**
     * 根据userid查询吐槽列表（分页）
     * @param userid
     * @param pageable
     * @returnll
     */
    public Page<Spitslot> findAllByUseridAndParentidIsNull(String userid, Pageable pageable);


}
