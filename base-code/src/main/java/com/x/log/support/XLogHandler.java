package com.x.log.support;

import com.x.base.GlobalCode;
import com.x.utils.DateTimeUtil;
import com.x.utils.ThreadLocalUtils;
import org.apache.commons.collections.map.HashedMap;

import java.util.Date;
import java.util.Map;

/**
 * @Author modongning
 * @updateBy modongning
 * @updateBy 2018/7/8 下午1:51
 * //TODO
 */
public class XLogHandler {

	public static Map<String, Object>  processLog(String logMsg, String level) {
		Map<String, Object> logInfo = ThreadLocalUtils.get(GlobalCode.REQUEST_INFO_THREAD_LOCAL_KEY, new HashedMap());
		logInfo.put("msg", logMsg);
		logInfo.put("level", level);
		logInfo.put("time", DateTimeUtil.date2Str(new Date(), DateTimeUtil.MILLIONSECONDS_PARTTEN));
		return logInfo;
	}

	public static Map<String, Object>  processLog(String logMsg, Exception e, String level) {
		Map<String, Object> logInfo = ThreadLocalUtils.get(GlobalCode.REQUEST_INFO_THREAD_LOCAL_KEY, new HashedMap());
		logInfo.put("msg", logMsg);
		logInfo.put("level", level);
		logInfo.put("exception", e);
		logInfo.put("time", DateTimeUtil.date2Str(new Date(), DateTimeUtil.MILLIONSECONDS_PARTTEN));
		return logInfo;
	}

}
