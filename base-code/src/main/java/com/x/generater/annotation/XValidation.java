package com.x.generater.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XValidation {
	boolean notNull() default false;
	int length();
	boolean number();
	boolean decimal();
	boolean mail();
	boolean phone();
	boolean mobile();
}
