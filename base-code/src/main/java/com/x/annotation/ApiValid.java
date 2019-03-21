package com.x.annotation;

import java.lang.annotation.*;

/**
 * 自定义API参数校验注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Documented
public @interface ApiValid {

	// 分组校验
	Class<?>[] group() default {};
}

