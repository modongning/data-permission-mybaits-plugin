package com.otoomo.datapermission.interceptor;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;

import java.util.Properties;

/**
 * 数据权限控制接口
 * <p>
 * 所有自定义数据权限控制实现该接口
 *
 * @Author modongning
 * @updateBy modongning
 * @updateBy 2018/8/20 上午10:28
 */
public interface DataPermissionInterceptor extends Interceptor {
	//step1 默认实现方法
	default void setProperties(Properties arg0) {
	}

	//step2 默认实现方法
	default Object plugin(Object arg0) {
		return Plugin.wrap(arg0, this);
	}

	//step3
	Object intercept(Invocation invocation) throws Throwable;
}
