package com.x.globalAdvice;

import com.x.exception.AbnormalParameterException;
import com.x.exception.AccountInvalidException;
import com.x.exception.BusinessException;
import com.x.exception.SessionTimeOutException;
import com.x.log.XLog;
import com.x.log.XLogFactory;
import com.x.response.Response;
import com.x.response.ResponseCode;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Locale;

/**
 * 全局异常处理。需要在依赖该组件的应用中开启@ComponentScan("com.XXX.globalAdvice")。
 * <p>
 * (1)@ControllerAdvice注解内部使用了：@ExceptionHandler/@InitBinder/
 *     对于未进入controller前的异常，该处理方法是无法进行捕获处理的，
 *     SpringBoot提供了ErrorController的处理类来处理所有的异常
 * @version 1.0.0
 * @ModelAttribute注解的方法，<br> 都会应用到所有Controller里@RequestMapping注解的方法上。<br>
 * (2)@PropertySource默认加载CLASSPATH下的exception.properties和exception.properties，
 * 可定义了异常描述。<br>
 */

@ControllerAdvice
public class GlobalExceptionHandler {
	private XLog log = XLogFactory.getInterface(GlobalExceptionHandler.class);

	/**
	 * 针对业务异常的处理。<br>
	 * 作为500错误，可在客户端AJAX请求的error()中捕获。<br>
	 *
	 * @param exception
	 * @param locale
	 * @return ExceptionInfo
	 */
	@ExceptionHandler(BusinessException.class)
	@ResponseBody // 与body对象绑定
	public Response handleBusinessException(Exception exception, Locale locale) {
		log(exception);
		return Response.error(ResponseCode.BUSINESS_ERROR, exception.getMessage());
	}

	/**
	 * 账号异常处理
	 *
	 * @param e
	 * @param locale
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler(AccountInvalidException.class)
	public Response handlerAccountInvalidException(Exception e, Locale locale) {
		log(e);
		return Response.error(ResponseCode.ACCOUNT_OR_PASSWORD_ERROR, e.getMessage());
	}

	/**
	 * API参数异常
	 *
	 * @param e
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler(AbnormalParameterException.class)
	public Response handlerAbnormalParameterException(Exception e) {
		log(e);
		return Response.error(ResponseCode.PARAM_ERROR, e.getMessage());
	}

	/**
	 * 针对Session过期处理。<br>
	 *
	 * @param exception
	 * @param locale
	 * @return ExceptionInfo
	 */
	@ExceptionHandler({SessionTimeOutException.class})
	@ResponseBody // 与body对象绑定
	public Response handleSessionTimeOutException(Exception exception, Locale locale) {
		log(exception);
		return Response.error(ResponseCode.SESSION_TIME, "会话已超时，请重新登陆");
	}

	/**
	 * TOKEN失效。<br>
	 *
	 * @param exception
	 * @param locale
	 * @return ExceptionInfo
	 */
	@ExceptionHandler({ExpiredCredentialsException.class})
	@ResponseBody // 与body对象绑定
	public Response handleExpiredCredentialsException(Exception exception, Locale locale) {
		log(exception);
		return Response.error(ResponseCode.SESSION_TIME, exception.getMessage());
	}

	/**
	 * Shiro认证账户异常
	 *
	 * @param exception
	 * @param locale
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler({AccountException.class})
	public Response handlerAccountException(Exception exception, Locale locale) {
		log(exception);
		return Response.error(ResponseCode.AUTHORIZATION_ERROR, exception.getMessage());
	}

	/**
	 * Shiro认证失败异常
	 *
	 * @param exception
	 * @param locale
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler({AuthenticationException.class})
	public Response handlerAuthenticationException(Exception exception, Locale locale) {
		log(exception);
		return Response.error(ResponseCode.AUTHENTICATION_ERROR, "认证失败");
	}

	/**
	 * Shiro 身份令牌异常，不支持的身份令牌
	 *
	 * @param exception
	 * @param locale
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler(UnsupportedTokenException.class)
	public Response handlerUnsupportedTokenException(Exception exception, Locale locale) {
		log(exception);
		return Response.error(ResponseCode.AUTHENTICATION_ERROR, exception.getMessage());
	}

	/**
	 * Shiro认证失败异常，密码错误
	 *
	 * @param exception
	 * @param locale
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler({IncorrectCredentialsException.class,UnknownAccountException.class})
	public Response handlerIncorrectCredentialsException(Exception exception, Locale locale) {
		log(exception);
		return Response.error(ResponseCode.AUTHENTICATION_ERROR, exception.getMessage());
	}

	/**
	 * Shiro授权失败异常
	 *
	 * @param exception
	 * @param locale
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler(AuthorizationException.class)
	public Response handlerAuthorizationException(Exception exception, Locale locale) {
		log(exception);
		return Response.error(ResponseCode.AUTHENTICATION_ERROR, "授权失败");
	}

	/**
	 * Shiro授权失败异常
	 *
	 * @param exception
	 * @param locale
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler(UnauthorizedException.class)
	public Response handlerUnauthorizedException(Exception exception, Locale locale) {
		log(exception);
		return Response.error(ResponseCode.AUTHORIZATION_ERROR, "没有权限");
	}

	/**
	 * 针对其他未知异常。
	 * <p>
	 * 作为500错误，可在客户端AJAX请求的error()中捕获。<br>
	 *
	 * @param exception
	 * @param locale
	 * @return ExceptionInfo
	 */
	@ExceptionHandler(Exception.class)
	@ResponseBody // 与body对象绑定
	public Response handleException(Exception exception, Locale locale) {
		log(exception);
		return Response.error(ResponseCode.UNKNOWN_ERROR, exception.getCause().getMessage());
	}

	/*
	 * 日志记录
	 */
	private void log(Exception exception) {
		log.error(null, exception);
	}
}
