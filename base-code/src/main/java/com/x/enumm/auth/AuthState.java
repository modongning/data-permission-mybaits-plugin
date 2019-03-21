package com.x.enumm.auth;

/**
 * 权限状态枚举
 * @author hzh 2018/7/18 下午10:29
 */
public enum AuthState {
	VALID(1), IN_VALID(2), DELETE(0);

	private Integer state;

	AuthState(Integer state) {
		this.state = state;
	}

	public Integer getState() {
		return state;
	}
}
