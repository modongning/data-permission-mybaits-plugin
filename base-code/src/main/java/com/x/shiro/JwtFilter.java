package com.x.shiro;

import com.alibaba.fastjson.JSONObject;
import com.x.log.XLog;
import com.x.log.XLogFactory;
import com.x.log.XLogType;
import com.x.response.Response;
import com.x.response.ResponseCode;
import com.x.utils.encrypt.AesEncodeUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Json Web Token 拦截器
 * 执行流程 preHandle -> isAccessAllowed -> isLoginAttempt -> executeLogin
 */
public class JwtFilter extends BasicHttpAuthenticationFilter {

	private XLog log = XLogFactory.getInterface(XLogType.LOG4J, JwtFilter.class);

	/**
	 * 判断用户是否是登录尝试
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		// 获取头信息Authorization
		Optional<String> ndAuthorization = Optional.ofNullable(httpServletRequest.getHeader("Authorization"));
		Optional<String> ndUser = Optional.ofNullable(httpServletRequest.getHeader("User"));
		// 如果Authorization不为空，则需要登录尝试
		return ndAuthorization.isPresent() && ndUser.isPresent();
	}

	/**
	 * 判断是否允许访问
	 *
	 * @param request
	 * @param response
	 * @param mappedValue
	 * @return
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		// 验证的请求
		if (!isLoginAttempt(request, response)) {
			writerResponse((HttpServletResponse) response, ResponseCode.AUTHENTICATION_ERROR, "无身份认证权限");
			return false;
		}
		// 登录认证
		try {
			return executeLogin(request, response);
		} catch (Exception e) {
			writerResponse((HttpServletResponse) response, ResponseCode.UNKNOWN_ERROR, e.getMessage());
			return false;
		}
	}

	/**
	 * 登录操作，调用Shiro的登录认证
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		// 获取头信息Nd_Authorization则是token
		String authorization = httpServletRequest.getHeader("Authorization");
		String user = httpServletRequest.getHeader("User");

		JwtToken jwtToken = new JwtToken(AesEncodeUtil.decrypt(authorization), user);

		// 提交给realm进行登入，如果错误他会抛出异常并被捕获
		Subject subject = SecurityUtils.getSubject();
		try {
			// 如果没有抛出异常则代表登入成功，返回true
			subject.login(jwtToken);
			return true;
		} catch (UnsupportedTokenException | ExpiredCredentialsException | AccountException e) {
			writerResponse((HttpServletResponse) response, ResponseCode.AUTHENTICATION_ERROR, e.getMessage());
		} catch (Exception e) {
			writerResponse((HttpServletResponse) response, ResponseCode.AUTHENTICATION_ERROR, e.getMessage());
		}
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		return false;
	}


	/**
	 * shiro错误统一返回
	 *
	 * @param response
	 * @param responseCode
	 * @param content
	 */
	private void writerResponse(HttpServletResponse response, ResponseCode responseCode, String content) {
		response.setHeader("Content-Type", "application/json;charset=utf-8");
		try {
			response.getWriter().write(JSONObject.toJSONString(Response.error(responseCode, content)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
