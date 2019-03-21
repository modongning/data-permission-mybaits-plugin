package com.x.validator;

import com.x.exception.AbnormalParameterException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * 参数检验工具
 *
 * @author hzh 2017-08-21 22:03
 */
public class ValidationHandler {

	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	/**
	 * 校验实体
	 * @param t
	 * @param groups
	 * @param <T>
	 */
	public static <T> void validateEntity(T t, Class<?>...groups) {
		Set<ConstraintViolation<T>> set = validator.validate(t, groups);
		if (null != set && !set.isEmpty()) {
			// 只返回第一个错误信息即可
			ConstraintViolation<T> constraintViolation = set.stream().findFirst().get();
			throw new AbnormalParameterException(constraintViolation.getMessage());
		}
	}

}
