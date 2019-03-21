package com.x.log;

import com.x.log.support.XSlf4j;
import com.x.log.support.XLog4j;

/**
 * @Author modongning
 * @updateBy modongning
 * @updateBy 2018/7/7 下午9:47
 * 日志实例化工厂
 */
public class XLogFactory {

	public static XLog getInterface(XLogType type, Class clazz) {
		switch (type) {
			case DEFAULT:
				return XSlf4j.getInterface(clazz);
			case LOG4J:
				return XLog4j.getInterface(clazz);
			case SLF4J:
				return XSlf4j.getInterface(clazz);
			case ALI:
				throw new IllegalStateException("日记类型未实现");
			case MQ2DB:
				throw new IllegalStateException("日记类型未实现");
			case MQ2COUNT:
				throw new IllegalStateException("日记类型未实现");
		}
		throw new IllegalArgumentException("类型错误");
	}

	public static XLog getInterface(Class clazz) {
		return getInterface(XLogType.DEFAULT, clazz);
	}
}
