package com.x.shiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.x.log.XLog;
import com.x.log.XLogFactory;
import com.x.log.XLogType;
import com.x.utils.encrypt.AesEncodeUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Jwt相关帮助类
 * @author hzh 2018/7/8 上午1:00
 */
@Component
public class JwtHelper {

	private XLog log = XLogFactory.getInterface(XLogType.SLF4J, JwtHelper.class);

	@Value("${my-shiro.jwt.time-out}")
	private Long expiredTime;

	/**
	 * 创建Json Web token
	 * @param identityId
	 * @param account
	 * @param password
	 * @return
	 */
	public String createToken(Long identityId, String account,String password) {
		try {
			return AesEncodeUtil.encrypt(JWT.create()
					.withClaim("identityId", identityId)
					.withClaim("account", account)
					.withExpiresAt(new Date(System.currentTimeMillis() + (this.expiredTime * 1000)))
					.sign(Algorithm.HMAC256(new Md5Hash(password, account, 3).toString())));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.error("创建Json Web token失败: {}", e);
			return null;
		}
	}

	/**
	 * 检查token是否为空
	 * @return
	 */
	public boolean checkToken() {
		return null != SecurityUtils.getSubject().getPrincipal() && !"".equals(SecurityUtils.getSubject().getPrincipal());
	}

	/**
	 * 获取token信息
	 * @return
	 */
	public JwtUser getUser() {
		// 获取请求token
		String token = (String) SecurityUtils.getSubject().getPrincipal();
		// 解析token
		DecodedJWT decodedJWT = JWT.decode(token);
		// 获取用户账号，由于拦截器中已做为空判断，这里暂时不做

		return new JwtUser(decodedJWT.getClaim("identityId").asLong(), decodedJWT.getClaim("account").asString());
	}

	/**
	 * 返回token有效时长
	 * @return
	 */
	public Long getExpiredTime() {
		return expiredTime;
	}
}
