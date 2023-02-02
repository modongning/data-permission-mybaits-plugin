package com.otoomo.datapermission.handler;

import com.otoomo.datapermission.annotation.DataPermission;

/**
 * 数据权限拼装处理器
 * <p>
 * 可实现该接口，对不同的需求，拼装对应业务的数据权限sql
 *
 * @Author modongning
 * @updateBy modongning
 * @updateBy 2018/8/24 下午4:37
 */
public interface DataPermissionHandler {

	/**
	 * 数据权限sql拼装处理
	 *
	 * @param alias                    表的别名 a
	 * @param scopeList                Mapper类注解的需要控制的数据范围列表
	 * @param sql                      基础的数据权限sql，如果不需要数据处理，直接返回sql；
	 *                                 如果需要处理数据权限，则最终拼接为： sql + 处理后的数据权限sql; 如：sql + WHERE 1=1 AND a.company_id IN (1,2)
	 * @param parameterObject          原sql参数
	 * @param userDataPermissionConfig 当前用户数据权限配置数据
	 * @return 拼装完成后的数据权限sql
	 */
	String handler(String alias, DataPermission.Scope[] scopeList, String sql, Object parameterObject, Object userDataPermissionConfig);
}
