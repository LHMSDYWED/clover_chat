package com.clover.user.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author jiangxin
 * @create 2019-01-14-21:48
 * 房屋信息的实体类
 */
@Entity
@Table(name="tb_house")
public class House implements Serializable {
    @Id
    private String id;//门牌ID



    private String housenumber;//门牌号
    private String mobile;//手机号
    private String name;//姓名
    private String idcard;//身份证号

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHousenumber() {
        return housenumber;
    }

    public void setHousenumber(String housenumber) {
        this.housenumber = housenumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }
}
