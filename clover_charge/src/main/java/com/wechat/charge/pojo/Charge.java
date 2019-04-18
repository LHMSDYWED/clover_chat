package com.wechat.charge.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * @author jiangxin
 * @create 2019-01-14-22:43
 * 费用信息的实体类
 */
@Entity
@Table(name="tb_charge")
public class Charge implements Serializable{

	@Id
	private String id;//id号


	
	private String hoserid;//门牌ID
	private String housenumber;//门牌号
	private Double watercharge;//水费
	private Double energycharge;//电费
	private Double gascharge;//气费
	private Double estatecharge;//物业费
	private Double totalcharge;//总费用
	private java.util.Date generationtime;//产生时间
	private java.util.Date paytime;//支付时间
	private String userid;//支付人ID
	private String username;//支付人姓名
	private String mobile; //支付人电话号码
	private Integer state;//状态，1：未支付、3.已支付、2.正在支付

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getHoserid() {		
		return hoserid;
	}
	public void setHoserid(String hoserid) {
		this.hoserid = hoserid;
	}

	public String getHousenumber() {		
		return housenumber;
	}
	public void setHousenumber(String housenumber) {
		this.housenumber = housenumber;
	}

	public Double getWatercharge() {		
		return watercharge;
	}
	public void setWatercharge(Double watercharge) {
		this.watercharge = watercharge;
	}

	public Double getEnergycharge() {		
		return energycharge;
	}
	public void setEnergycharge(Double energycharge) {
		this.energycharge = energycharge;
	}

	public Double getGascharge() {		
		return gascharge;
	}
	public void setGascharge(Double gascharge) {
		this.gascharge = gascharge;
	}

	public Double getEstatecharge() {		
		return estatecharge;
	}
	public void setEstatecharge(Double estatecharge) {
		this.estatecharge = estatecharge;
	}

	public Double getTotalcharge() {		
		return totalcharge;
	}
	public void setTotalcharge(Double totalcharge) {
		this.totalcharge = totalcharge;
	}

	public java.util.Date getGenerationtime() {		
		return generationtime;
	}
	public void setGenerationtime(java.util.Date generationtime) {
		this.generationtime = generationtime;
	}

	public java.util.Date getPaytime() {		
		return paytime;
	}
	public void setPaytime(java.util.Date paytime) {
		this.paytime = paytime;
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

	public Integer getState() {		
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}


	
}
