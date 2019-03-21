package com.x.enumm.user;

import java.util.Arrays;
import java.util.Optional;

/**
 * 系统用户状态
 * @author hzh 2018/7/14 下午5:17
 */
public enum SystemUserState {
	VALID(1), IN_VALID(2), DELETE(0);

	private Integer state;

	SystemUserState(Integer state) {
		this.state = state;
	}

	public Integer getState() {
		return state;
	}

	public static Optional<SystemUserState> get(Integer state) {
		return Arrays.stream(SystemUserState.values()).filter(systemUserState -> state.equals(systemUserState.state)).findFirst();
	}
}
