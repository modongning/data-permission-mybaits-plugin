package com.x.datapermission.annotation;

import java.lang.annotation.*;

/**
 * 数据权限控制注解
 *
 * @Author modongning
 * @updateBy modongning
 * @updateBy 2018/8/16 下午5:02
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataPermission {

	//别名
	String alias();

	//数据控制范围，默认控制partnerId数据
	Scope[] scope() default {@DataPermission.Scope(key = "company_id", mapping = Mapping.COMPANY)};

	FilterMethod[] filter() default {};

	String dataPermissionHandler() default "defaultDataPermissionHandler";

	/**
	 * 需要数据权限拦截的方法配置
	 */
	@interface FilterMethod {
		String alias() default "";

		String name();

		Scope[] scope() default {};
	}

	/*
		数据权限控制范围
	 */
	@interface Scope {
		//特殊指定权限KEY
		String key();

		Mapping mapping();

		IgnoreWhen[] ignore() default {};

		// 多个数据权限控制时关联拼接条件的关键字
		// 如：partenrId IN (...) [joinType] subPartnerId IN (...)
		JoinType joinType() default JoinType.AND;
	}

	/**
	 * 当查询参数包含在IgnoreWhen的配置中时，忽略当前配置的数据权限的拦截
	 */
	@interface IgnoreWhen {
		String key();

		String value() default "";
	}

	/**
	 * 数据权限控制的类型
	 */
	enum Mapping {
		COMPANY,
	}

	/**
	 * 数据获取比较方式
	 */
	enum JoinType {
		//等于
		EQ(" = "),
		//小于
		LT(" < "),
		//大于
		GT(" > "),
		//IN
		IN(" IN "),
		OR(" OR "),
		AND(" AND ");

		private String value;

		JoinType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

}
