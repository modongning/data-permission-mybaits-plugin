package com.x.log;

/**
 * @Author modongning
 * @updateBy modongning
 * @updateBy 2018/7/7 下午10:20
 */
public enum XLogType {
	DEFAULT,
	//log4j日志
	LOG4J,
	//slf4j日志
	SLF4J,
	//阿里云日志
	ALI,
	//MQ方式写日志到数据库
	MQ2DB,
	//MQ方式统计数量日志
	MQ2COUNT
}
