package com.wechat.dynamic.pojo;



import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
/**
 * @author jiangxin
 * @create 2019-01-14-23:05
 * 小区动态的实体类
 */
@Entity
@Table(name="tb_dynamic")
public class Dynamic implements Serializable{

	@Id
	private String id;//ID号


	
	private String theme;//主题
	private String content;//类容
	private String images;//图片
	private java.util.Date createtime;//发布日期
	private java.util.Date updatetime;//修改日期
	private Integer istop;//1：置顶，2：不置顶
	private Integer state;//状态，1：未开始、2：正在进行、3：已结束
	private Integer visits;//访问量
	private Integer thumbup;//点赞数
	private Integer comment;//评论数
	@Transient
	private Boolean flag; //判断目前用户是否点赞，不用存数据库去
	@Transient
	private final static Boolean showReplay = false; //评论框是否弹出 ,这里默认为不

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public static Boolean getShowReplay() {
		return showReplay;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getTheme() {		
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
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

	public java.util.Date getCreatetime() {		
		return createtime;
	}
	public void setCreatetime(java.util.Date createtime) {
		this.createtime = createtime;
	}

	public java.util.Date getUpdatetime() {		
		return updatetime;
	}
	public void setUpdatetime(java.util.Date updatetime) {
		this.updatetime = updatetime;
	}

	public Integer getIstop() {		
		return istop;
	}
	public void setIstop(Integer istop) {
		this.istop = istop;
	}

	public Integer getState() {		
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getVisits() {		
		return visits;
	}
	public void setVisits(Integer visits) {
		this.visits = visits;
	}

	public Integer getThumbup() {		
		return thumbup;
	}
	public void setThumbup(Integer thumbup) {
		this.thumbup = thumbup;
	}

	public Integer getComment() {		
		return comment;
	}
	public void setComment(Integer comment) {
		this.comment = comment;
	}


	
}
