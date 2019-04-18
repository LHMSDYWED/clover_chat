package com.wechat.repair.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * @author jiangxin
 * @create 2019-01-14-23:47
 * 报修信息的实体类
 */
@Entity
@Table(name="tb_repair")
public class Repair implements Serializable{

	@Id
	private String id;//ID号


	
	private Integer type;//类型，1：私有、2：共有
	private String content;//内容
	private String images;//图片
	private java.util.Date repairtime;//报修时间
	private java.util.Date handletime;//处理时间
	private String userid;//用户ID
	private String username;//用户姓名
	private String mobile;//手机号
	private Integer state;//状态，1：未处理、2：已处理、3：正在处理

	
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

	public java.util.Date getRepairtime() {		
		return repairtime;
	}
	public void setRepairtime(java.util.Date repairtime) {
		this.repairtime = repairtime;
	}

	public java.util.Date getHandletime() {		
		return handletime;
	}
	public void setHandletime(java.util.Date handletime) {
		this.handletime = handletime;
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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

    @Override
    public String toString() {
        return "Repair{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", images='" + images + '\'' +
                ", repairtime=" + repairtime +
                ", handletime=" + handletime +
                ", userid='" + userid + '\'' +
                ", username='" + username + '\'' +
                ", mobile='" + mobile + '\'' +
                ", state=" + state +
                '}';
    }
}
