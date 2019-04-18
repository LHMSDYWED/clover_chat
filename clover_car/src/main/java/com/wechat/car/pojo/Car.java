package com.wechat.car.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * @author jiangxin
 * @create 2019-01-14-22:20
 * 车位、车辆信息的实体类
 */
@Entity
@Table(name="tb_car")
public class Car implements Serializable{

	@Id
	private String id;//id号


	
	private String carport;//车位号
	private String carid;//车牌号
	private String name;//车主名
	private String mobile;//手机号
	private Integer type;//类型，1：购买、2：租用、3：临时
	private java.util.Date usedate;//使用期限
	private java.util.Date starttime;//开始时间
	private java.util.Date leavetime;//离开时间
	private Double unitprice;//单价，每小时
	private Double pay;//需支付的费用
	private Integer state;// 状态 1:正在使用 2：空闲 3：故障
	private String userid;//车主ID号

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getCarport() {		
		return carport;
	}
	public void setCarport(String carport) {
		this.carport = carport;
	}

	public String getCarid() {		
		return carid;
	}
	public void setCarid(String carid) {
		this.carid = carid;
	}

	public String getName() {		
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {		
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getType() {		
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}

	public java.util.Date getUsedate() {		
		return usedate;
	}
	public void setUsedate(java.util.Date usedate) {
		this.usedate = usedate;
	}

	public java.util.Date getStarttime() {		
		return starttime;
	}
	public void setStarttime(java.util.Date starttime) {
		this.starttime = starttime;
	}

	public java.util.Date getLeavetime() {		
		return leavetime;
	}
	public void setLeavetime(java.util.Date leavetime) {
		this.leavetime = leavetime;
	}

	public Double getUnitprice() {		
		return unitprice;
	}
	public void setUnitprice(Double unitprice) {
		this.unitprice = unitprice;
	}

	public Double getPay() {		
		return pay;
	}
	public void setPay(Double pay) {
		this.pay = pay;
	}

	public Integer getState() {		
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}


	
}
