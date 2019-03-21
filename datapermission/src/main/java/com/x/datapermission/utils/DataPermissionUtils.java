package com.x.datapermission.utils;

import com.x.datapermission.annotation.DataPermission;
import org.apache.ibatis.mapping.MappedStatement;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 数据权限工具类
 *
 * @Author modongning
 * @updateBy modongning
 * @updateBy 2018/8/16 下午5:01
 */
public class DataPermissionUtils {

	/**
	 * 获取方法上的数据权限注解
	 *
	 * @param mappedStatement
	 * @return com.x.datapermission.annotation.DataPermission
	 */
	public static DataPermission getClassPermission(MappedStatement mappedStatement) {
		DataPermission dataPermission = null;
		try {
			String id = mappedStatement.getId();
			String className = id.substring(0, id.lastIndexOf("."));

			final Class cls = Class.forName(className);

			if (cls.getName().equals(className) && cls.isAnnotationPresent(DataPermission.class)) {
				dataPermission = (DataPermission) cls.getAnnotation(DataPermission.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataPermission;
	}

	/**
	 * 获取方法名
	 *
	 * @param mappedStatement
	 * @return
	 */
	public static String getMethodName(MappedStatement mappedStatement) {
		try {
			String id = mappedStatement.getId();

			return id.substring(id.lastIndexOf(".") + 1, id.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
