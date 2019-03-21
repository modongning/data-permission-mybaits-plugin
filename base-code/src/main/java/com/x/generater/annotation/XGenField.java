package com.x.generater.annotation;

import com.x.generater.core.Default;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XGenField {
	String comment() default "";
	boolean key() default false;
	boolean autoIncrease() default false;
	int length() default 0;
	String codeType() default "";
	boolean notNull() default false;
	boolean query() default false;
	boolean index() default false;
	boolean uniqueKey() default false;
	boolean bigText() default false;
	boolean ascOrder() default false;
	boolean descOrder() default false;
	Default def() default Default._DEFAULT;
}
