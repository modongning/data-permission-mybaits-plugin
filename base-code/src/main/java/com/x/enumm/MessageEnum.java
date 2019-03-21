package com.x.enumm;

/**
 * 系统消息类型枚举
 * @author hzh 2018/7/12 下午9:05
 */
public enum MessageEnum {
	SYSTEM(1, "系统消息");

	private Integer type;
	private String name;

	MessageEnum(Integer type, String name) {
		this.type = type;
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public String getName() {
		return name;
	}
}
