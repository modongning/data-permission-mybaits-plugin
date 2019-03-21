package com.x.globalAdvice;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.x.base.GlobalCode;
import com.x.interceptor.ParameterInterceptor;
import com.x.utils.DateTimeUtil;
import com.x.utils.HttpUtils;
import com.x.utils.ThreadLocalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 全局请求拦截器。
 */

@Configuration
public class GlobalRequestHandler extends WebMvcConfigurerAdapter {
	private static Logger log = LoggerFactory.getLogger(GlobalRequestHandler.class);

	/**
	 * JSON解析器配置
	 *
	 * @param converters
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

		ObjectMapper om = new ObjectMapper();
		// null替换为""

		om.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
			@Override
			public void serialize(Object arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException, JsonProcessingException {
				arg1.writeString("");
			}
		});

		om.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		//反序列化时忽略在JSON字符串中存在，而在Java中不存在的属性
		om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		SimpleModule simpleModule = new SimpleModule();

//		 Long 转字符串
		simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
		simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);

		om.registerModule(simpleModule);

		converter.setObjectMapper(om);

		converters.add(converter);

		super.configureMessageConverters(converters);
	}

	@Bean
	public TransTrackHandler getTransTrackHandler() {
		return new TransTrackHandler();
	}

	@Bean
	public ParameterInterceptor parameterInterceptor() {
		return new ParameterInterceptor();
	}

	/**
	 * 需要注册拦截器，否则拦截器无法运行。 并在此处配置拦截的PathPatterns。
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration addInterceptor = registry.addInterceptor(getTransTrackHandler());

		// 排除配置
//		addInterceptor.excludePathPatterns("/login");

		// 参数校验拦截器
		registry.addInterceptor(parameterInterceptor()).addPathPatterns("/**");
	}

	private class TransTrackHandler extends HandlerInterceptorAdapter {

		/**
		 * 请求前置处理
		 *
		 * @param request
		 * @param response
		 * @param handler
		 * @return
		 * @throws Exception
		 */
		@Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
			String transId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
			String transTime = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DAYTIME_PARTTEN);
			String requestIp = HttpUtils.getRequestIp(request);
			String requestUrl = HttpUtils.getRequestUrl(request);

			Map<String, String> requestInfo = new HashMap<>();
			requestInfo.put("transId", transId);
			requestInfo.put("requestIp", requestIp);
			requestInfo.put("requestUrl", requestUrl);
			requestInfo.put("transTime", transTime);
			//设置请求事件ID，后期日期跟踪
			ThreadLocalUtils.set(GlobalCode.REQUEST_INFO_THREAD_LOCAL_KEY, requestInfo);

			return true;
		}

		/**
		 * 请求完成处理
		 *
		 * @param httpServletRequest
		 * @param httpServletResponse
		 * @param o
		 * @param e
		 * @throws Exception
		 */
		@Override
		public void afterCompletion(HttpServletRequest httpServletRequest,
		                            HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
			//请求完成清除线程变量
			ThreadLocalUtils.remove();
		}
	}
}
