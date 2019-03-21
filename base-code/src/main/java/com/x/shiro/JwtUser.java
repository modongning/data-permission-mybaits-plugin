package com.x.shiro;

import lombok.Data;

/**
 * Jwt token 用户信息
 * @author hzh 2018/7/9 下午9:26
 */
@Data
public class JwtUser {

	/**
	 *用户ID
	 */
	private Long identityId;
	/**
	 * 账号
	 */
	private String account;

	public JwtUser(Long identityId, String account) {
		this.identityId = identityId;
		this.account = account;
	}
}
