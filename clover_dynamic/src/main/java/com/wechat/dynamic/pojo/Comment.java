package com.wechat.dynamic.pojo;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * @author jiangxin
 * @create 2019-01-18-17:48
 * 小区评论信息的实体类
 */
public class Comment implements Serializable {

    @Id
    private String _id; //mongodb数据库的主键，前面必须加一个_；

    private String userid;  //评论人的ID
    private String dynamicid; //被评论动态的ID
    private String comment; //评论的信息

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
