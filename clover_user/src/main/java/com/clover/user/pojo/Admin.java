package com.clover.user.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author jiangxin
 * @create 2019-01-14-21:46
 * 管理员的实体类
 */
@Entity
@Table(name="tb_admin")
public class Admin implements Serializable {
    @Id
    private String id;//id号



    private String loginname;//用户名
    private String password;//密码
    private Integer state;//状态，1：在线、2：离线

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
