package com.x.log;

/**
 * @Author modongning
 * @updateBy modongning
 * @updateBy 2018/7/7 下午8:23
 * 日志通用接口
 * 实现该类实现不同的日志功能
 */
public interface XLog {

	String INFO = "INFO";
	String ERROR = "ERROR";

	void info(String infoMsg);

	void info(String infoMsg, Object... args);

	void error(String errorMsg);

	void error(String errorMsg, Object... args);

	void error(String errorMsg, Exception e);
}
