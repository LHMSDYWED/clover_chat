package com.wechat.spitslot.pojo;


import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jiangxin
 * @create 2019-01-15-00:07
 * 吐槽信息的实体类
 */
//@Entity
//@Table(name="tb_spitslot")
public class Spitslot implements Serializable{

	@Id
	private String _id;//id号


	
	private String content;//内容
	private java.util.Date publishtime;//发布时间
	private String userid; //发布人id
	private String nickname;//发布人昵称
	private String images;//图片
	private Integer comment;//评论数/回复数
	private Integer thumbup;//点赞
	private Integer vistis;//访问量
	private String state; //是否可见 1：可见 2：不可见
	private String parentid; //上级id
    private Boolean flag; //判断目前用户是否点赞，不用存数据库去
    private final static Boolean showReplay = false; //评论框是否弹出 ,这里默认为不



    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getPublishtime() {
		return publishtime;
	}

	public void setPublishtime(Date publishtime) {
		this.publishtime = publishtime;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public Integer getComment() {
		return comment;
	}

	public void setComment(Integer comment) {
		this.comment = comment;
	}

	public Integer getThumbup() {
		return thumbup;
	}

	public void setThumbup(Integer thumbup) {
		this.thumbup = thumbup;
	}

	public Integer getVistis() {
		return vistis;
	}

	public void setVistis(Integer vistis) {
		this.vistis = vistis;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
}
