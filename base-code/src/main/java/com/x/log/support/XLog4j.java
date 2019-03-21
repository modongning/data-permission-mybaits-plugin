package com.x.log.support;

import com.x.log.XLog;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Author modongning
 * @updateBy modongning
 * @updateBy 2018/7/7 下午8:47
 * Log4j2
 */
public class XLog4j implements XLog {

	private Logger logger;

	private XLog4j(Class clazz) {
		this.logger = LogManager.getLogger(clazz);
	}

	public static XLog4j getInterface(Class clazz) {
		return new XLog4j(clazz);
	}

	@Override
	public void info(String infoMsg) {
		logger.info(infoMsg);
	}

	public void info(String infoMsg, Object... args) {
		logger.info(infoMsg, args);
	}

	@Override
	public void error(String errorMsg) {
		logger.error(errorMsg);
	}

	public void error(String errorMsg, Object... args) {
		logger.error(errorMsg, args);
	}

	@Override
	public void error(String errorMsg, Exception e) {
		logger.error(errorMsg, e);
	}
}
