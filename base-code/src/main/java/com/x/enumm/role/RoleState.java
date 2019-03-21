package com.x.enumm.role;

/**
 * 角色状态枚举
 * @author hzh 2018/7/16 下午9:55
 */
public enum RoleState {

	VALID(1), IN_VALID(2), DELETE(0);

	private Integer state;

	RoleState(Integer state) {
		this.state = state;
	}

	public Integer getState() {
		return state;
	}

}
