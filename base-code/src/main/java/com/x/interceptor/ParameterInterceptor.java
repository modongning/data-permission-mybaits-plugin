package com.x.interceptor;

import com.x.annotation.ApiValid;
import com.x.validator.ValidationHandler;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * 接口参数校验拦截器
 * @author hzh 2018/7/8 下午2:01
 */
public class ParameterInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
		// 放行指定请求
		String requestMethod = request.getMethod();
		if (requestMethod.equals(HttpMethod.OPTIONS+"")) {
			return true;
		}

		// 获取请求数据
		Map<String, String[]> parameterMap = request.getParameterMap();
		Map<String, String> params = new HashMap<>(16);

		Set<String> paramKeySet = parameterMap.keySet();
		paramKeySet.stream().forEach(key -> params.put(key, parameterMap.get(key)[0]));

		// 获取请求方法参数，进行参数校验
		HandlerMethod method = (HandlerMethod) o;

		this.paramsValidator(method, params, paramKeySet, request);

		return true;
	}

	/**
	 * 参数校验
	 * @param method
	 * @param params
	 * @param paramNameSet
	 * @return
	 * @throws Exception
	 * @author hzh
	 */
	private void paramsValidator(HandlerMethod method, Map<String, String> params, Set<String> paramNameSet, HttpServletRequest request) throws Exception {
		MethodParameter[] methodParameters = method.getMethodParameters();
		for (MethodParameter methodParameter : methodParameters) {
			// 判断是否拥有@ApiValid注解
			ApiValid apiValid = methodParameter.getParameterAnnotation(ApiValid.class);
			if (null != apiValid) {
				// 获取参数类型
				Class<?> parameterType = methodParameter.getNestedParameterType();
				Object object = parameterType.newInstance();

				// 设置对象字段值
				setFieldValue(params, paramNameSet, object, request);

				ValidationHandler.validateEntity(object, apiValid.group());
			}
		}
	}

	/**
	 * 设置对象字段值
	 * @param params
	 * @param paramNameSet
	 * @param object
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @author hzh
	 */
	private void setFieldValue(Map<String, String> params, Set<String> paramNameSet, Object object, HttpServletRequest request) throws NoSuchFieldException, IllegalAccessException {
		List<Field> fieldList = new ArrayList<>();
		Class<?> clazz = object.getClass();
		for (;Object.class != clazz; clazz = clazz.getSuperclass()) {
			Field[] fields = clazz.getDeclaredFields();
			fieldList.addAll(Arrays.asList(fields));
		}

		for (String paramName : paramNameSet) {
			boolean isExits = false;
			for (Field field : fieldList) {
				if (field.getName().equals(paramName)) {
					isExits = true;
				}
			}

			if (!isExits) {
				continue;
			}

			Field field = object.getClass().getDeclaredField(paramName);
			String paramValue = params.get(paramName);
			if (null != field && !"".equals(paramValue)) {
				String fieldType = field.getGenericType().toString();
				field.setAccessible(true);
				if ("class java.lang.Long".equals(fieldType)) {
					field.set(object, Long.parseLong(paramValue));
				} else if ("class java.lang.String".equals(fieldType)) {
					field.set(object, paramValue);
				} else if ("class java.lang.Integer".equals(fieldType)) {
					field.set(object, Integer.parseInt(paramValue));
				} else if ("class java.math.BigDecimal".equals(fieldType)) {
					field.set(object, new BigDecimal(paramValue));
				}
			}
		}
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

	}
}
