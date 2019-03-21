package com.x.enumm.user;

import java.util.Arrays;
import java.util.Optional;

/**
 * 第三方账号类型枚举
 * @author hzh 2018/7/12 下午8:42
 */
public enum ThirdPartyEnum {
	WE_CHAT(1, "微信");

	private Integer type;
	private String name;

	ThirdPartyEnum(Integer type, String name) {
		this.type = type;
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public static Optional<ThirdPartyEnum> get(Integer type) {
		return Arrays.stream(ThirdPartyEnum.values()).filter(thirdPartyEnum -> type.equals(thirdPartyEnum.type)).findFirst();
	}
}
