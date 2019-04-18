package com.clover.user.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author jiangxin
 * @create 2019-01-14-21:43
 * 用户的实体类
 */
@Entity
@Table(name="tb_user")
public class User implements Serializable {
    @Id
    private String id;//id号



    private String openid;//微信id
    private String mobile;//手机号
    private String nickname;//微信昵称
    private String name; //姓名
    private String avatar;//头像
    private java.util.Date regdate;//注册日期
    private java.util.Date updatedate;//修改日期
    private Integer sex;//性别，1：男 、2：女
    private String carid;//车牌号
    private String houseid;//房间号
    private java.util.Date lastdate;//最后登录日期
    private String password; //密码
    private Integer state; //状态，1：临时用户、2：小区用户

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getRegdate() {
        return regdate;
    }

    public void setRegdate(Date regdate) {
        this.regdate = regdate;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getCarid() {
        return carid;
    }

    public void setCarid(String carid) {
        this.carid = carid;
    }

    public String getHouseid() {
        return houseid;
    }

    public void setHouseid(String houseid) {
        this.houseid = houseid;
    }

    public Date getLastdate() {
        return lastdate;
    }

    public void setLastdate(Date lastdate) {
        this.lastdate = lastdate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", openid='" + openid + '\'' +
                ", mobile='" + mobile + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", regdate=" + regdate +
                ", updatedate=" + updatedate +
                ", sex=" + sex +
                ", carid='" + carid + '\'' +
                ", houseid='" + houseid + '\'' +
                ", lastdate=" + lastdate +
                ", password='" + password + '\'' +
                '}';
    }
}
