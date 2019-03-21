package com.x.globalAdvice;

import com.x.annotation.ResponseIntercept;
import com.x.base.GlobalCode;
import com.x.response.Response;
import com.x.utils.ThreadLocalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Map;

/**
 * 全局Response对象处理，以标准化的JSON格式输出，包括：
 * <p>
 * 1-所有从Controller中成功返回的信息。<br>
 * 2-在{@link GlobalExceptionHandler}中返回的异常信息。<br>
 *
 * @version v1.0.0
 */

@ControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {
	private static Logger log = LoggerFactory.getLogger(GlobalResponseHandler.class);

	/**
	 * 返回前置处理，如果包含注解ResponseIntercept并值为false，则不拦截，按数据放回
	 */
	@Override
	public boolean supports(MethodParameter returnType, @SuppressWarnings("rawtypes") Class converterType) {
		ResponseIntercept responseIntercept = returnType.getMethod().getAnnotation(ResponseIntercept.class);
		if (null != responseIntercept) {
			return responseIntercept.value();
		}
		return true;
	}

	/**
	 * Controller及ControllerAdvice返回后，对结果(body)进行封装
	 * 如果返回的类型不是ApiResponse，则进行封装
	 */
	@Override
	public Object beforeBodyWrite(Object body,
	                              MethodParameter returnType,
	                              MediaType selectedContentType,
	                              @SuppressWarnings("rawtypes") Class selectedConverterType,
	                              ServerHttpRequest request, ServerHttpResponse response) {

		if (!(body instanceof Response)) {
			body = Response.ok(body);
		}

		Response res = (Response) body;
		Map<String,String> requestInfo = ThreadLocalUtils.get(GlobalCode.REQUEST_INFO_THREAD_LOCAL_KEY);
		if (null != requestInfo) {
			String transId = requestInfo.get("transId");
			String transTime = requestInfo.get("transTime");
			String requestIp = requestInfo.get("requestIp");
			String requestUrl = requestInfo.get("requestUrl");

			res.setTransId(transId);
			res.setTransTime(transTime);
			res.setRequestIp(requestIp);
			res.setRequestUrl(requestUrl);
		}

		return res;
	}
}
