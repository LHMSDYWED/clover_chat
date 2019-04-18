package com.wechat.feedback.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * @author jiangxin
 * @create 2019-01-14-23:27
 * 反馈信息的实体类
 */
@Entity
@Table(name="tb_feedback")
public class Feedback implements Serializable{

	@Id
	private String id;//id号


	
	private Integer type;//类型，1：投诉、2：表扬、3：建议
	private String content;//内容
	private String images;//图片
	private java.util.Date backtime;//反馈时间
	private java.util.Date handletime;//处理时间
	private Integer state;//状态，1：未处理、3：已处理、2：正在处理
	private String userid;//反馈人ID
	private String username;//反馈人姓名
	private String mobile;//手机号

	
	public String getId() {		
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public Integer getType() {		
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}

	public String getContent() {		
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public String getImages() {		
		return images;
	}
	public void setImages(String images) {
		this.images = images;
	}

	public java.util.Date getBacktime() {		
		return backtime;
	}
	public void setBacktime(java.util.Date backtime) {
		this.backtime = backtime;
	}

	public java.util.Date getHandletime() {		
		return handletime;
	}
	public void setHandletime(java.util.Date handletime) {
		this.handletime = handletime;
	}

	public Integer getState() {		
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}

	public String getUserid() {		
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {		
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getMobile() {		
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	
}
