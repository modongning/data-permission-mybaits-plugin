package com.x.datapermission;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.x.datapermission.handler.DataPermissionHandler;
import com.x.datapermission.interceptor.DataPermissionInterceptor;
import com.x.datapermission.handler.DefaultDataPermissionHandler;
import com.x.datapermission.interceptor.DefaultDataPermissionInterceptor;

/**
 * 数据权限组件配置
 *
 * @Author modongning
 * @updateBy modongning
 * @updateBy 2018/8/20 上午10:32
 */
@Configuration
public class DataPermissionConfig {

	/**
	 * 默认数据权限控制实现
	 *
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(name = "dataPermissionInterceptor") //如果已存在dataPermissionInterceptor的bean,则不再实例化默认的实现
	public DataPermissionInterceptor dataPermissionInterceptor() {
		return new DefaultDataPermissionInterceptor();
	}

	@Bean("defaultDataPermissionHandler")
	public DataPermissionHandler dataPermissionHandler() {
		return new DefaultDataPermissionHandler();
	}
}
