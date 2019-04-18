package com.wechat.userinfo.pojo;

import java.io.Serializable;

/**
 * @author jiangxin
 * @create 2019-01-25-20:44
 * 微信用户信息的实体类
 */
public class UserInfo implements Serializable {

    private String openid;      //用户唯一标识
    private String session_key; //会话密钥
    private String unionid;     //用户在开放平台的唯一标识符
    private Integer errcode;    //错误码
    private String errmsg;      //错误信息

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
