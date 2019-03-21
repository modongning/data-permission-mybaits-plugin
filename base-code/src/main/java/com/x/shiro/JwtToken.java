package com.x.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 实现Shiro 认证token
 * @author hzh 2018/7/7 下午4:40
 */
public class JwtToken implements AuthenticationToken{

	private String token;
	private String user;

	public JwtToken(String token) {
		this.token = token;
	}

	public JwtToken(String token, String user) {
		this.token = token;
		this.user = user;
	}

	public String getUser() {
		return user;
	}

	@Override
	public Object getPrincipal() {
		return this.token;
	}

	@Override
	public Object getCredentials() {
		return this.token;
	}
}
