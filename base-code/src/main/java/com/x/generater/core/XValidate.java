package com.x.generater.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XValidate {
	// 0-校验字段名
	String comment() default "";

	// 1-非空校验
	boolean notNull() default false;

	// 2-最大长度校验
	int maxLen() default 0;

	// 3-最小长度校验
	int minLen() default 0;

	// 4-整数校验
	boolean numeric() default false;

	// 5-小数校验(4位)
	boolean decimal() default false;

	// 6-邮箱格式校验
	boolean mail() default false;

	// 7-固定电话格式校验
	boolean phone() default false;

	// 8-移动电话格式校验
	boolean mobile() default false;

	// 9-IP地址校验
	boolean ip() default false;

	// 10-URL校验
	boolean URL() default false;
	
	// 11-身份证校验
	boolean idCard() default false;
	
	// 12-密码校验
	boolean password() default false;
	
	// 13-证件号码校验
	boolean certify() default false;
	
	// 14-车牌号校验
	boolean carNo() default false;
}
