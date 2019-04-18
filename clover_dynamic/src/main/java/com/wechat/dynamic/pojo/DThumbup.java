package com.wechat.dynamic.pojo;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * @author jiangxin
 * @create 2019-01-18-13:35
 * 小区动态点赞信息的实体类
 */
public class DThumbup implements Serializable {
    @Id
    private String _id; //mongodb数据库的主键，前面必须加一个_；

    private String userid;  //点赞人的ID
    private String dynamicid; //被点赞动态的ID

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDynamicid() {
        return dynamicid;
    }

    public void setDynamicid(String dynamicid) {
        this.dynamicid = dynamicid;
    }

    @Override
    public String toString() {
        return "DThumbup{" +
                "_id='" + _id + '\'' +
                ", userid='" + userid + '\'' +
                ", dynamicid='" + dynamicid + '\'' +
                '}';
    }
}
