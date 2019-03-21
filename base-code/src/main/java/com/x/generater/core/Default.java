package com.x.generater.core;

/**
 * @Author modongning
 * @updateBy modongning
 * @updateBy 2018/7/6 下午1:54
 * //TODO
 */
public enum Default {

	_DEFAULT(-1),
	DEFAULT_INT_ZERO(0),
	DEFAULT_INT_ONE(1),
	DEFAULT_STR_EMPTY(""),
	DEFAULT_STR_NULL(null);

	private Object value;

	Default(Object value) {
		this.value = value;
	}

	/**
	 * 覆盖
	 * @return
	 */
	@Override
	public String toString() {
		return value+"";
	}
}
