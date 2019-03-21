package com.x.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.x.generater.annotation.XGenField;
import com.x.generater.core.Default;

import java.util.Date;

/**
 * model基类
 */
public class BaseModel {

	/**
	 * 表字段：sys_user.create_time 注释：创建时间
	 */
	@XGenField(comment = "创建时间")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;
	/**
	 * 表字段：sys_user.modify_time 注释：修改时间
	 */
	@XGenField(comment = "修改时间")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date modifyTime;
	/**
	 * 表字段：sys_user.create_user_id 注释：创建人ID
	 */
	@XGenField(comment = "创建人")
	private Long createUserId;
	/**
	 * 表字段：sys_user.modify_user_id 注释：修改人ID
	 */
	@XGenField(comment = "修改人")
	private Long modifyUserId;
	/**
	 * 表字段：sys_user.state 注释：状态（1：使用中，2：暂停使用，0：删除）
	 */
	@XGenField(comment = "状态",def = Default.DEFAULT_INT_ONE,length = 2)
	private Integer state;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Long getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(Long modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
}
