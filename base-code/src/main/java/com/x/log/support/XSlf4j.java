package com.x.log.support;

import com.alibaba.fastjson.JSON;
import com.x.log.XLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @Author modongning
 * @updateBy modongning
 * @updateBy 2018/7/7 下午10:27
 * Log4j
 */
public class XSlf4j implements XLog {

	private Logger logger;

	private XSlf4j(Class clazz) {
		this.logger = LoggerFactory.getLogger(clazz);
	}

	public static XSlf4j getInterface(Class clazz) {
		return new XSlf4j(clazz);
	}

	@Override
	public void info(String infoMsg) {
		handlerLog(XLogHandler.processLog(infoMsg,INFO),INFO);
	}

	@Override
	public void info(String infoMsg, Object... args) {
		handlerLog(XLogHandler.processLog(infoMsg,INFO),INFO,args);
	}

	@Override
	public void error(String errorMsg) {
		handlerLog(XLogHandler.processLog(errorMsg,ERROR),ERROR);
	}

	@Override
	public void error(String errorMsg, Object... args) {
		handlerLog(XLogHandler.processLog(errorMsg,ERROR),ERROR,args);
	}

	@Override
	public void error(String errorMsg, Exception e) {
		e.printStackTrace();
		handlerLog(XLogHandler.processLog(errorMsg,e,ERROR),ERROR);
	}

	private void handlerLog(Map<String, Object> processLog,String level,Object... args){
		switch (level) {
			case INFO:
				logger.info(JSON.toJSONString(processLog.toString()),args);
				break;
			case ERROR:
				logger.error(JSON.toJSONString(processLog.toString()),args);
				break;
		}
	}
}
