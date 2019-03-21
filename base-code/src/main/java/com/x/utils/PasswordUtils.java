package com.x.utils;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * @Author modongning
 * @updateBy modongning
 * @updateBy 2018/8/3 下午2:11
 */
public class PasswordUtils {

	/**
	 * @author modongning
	 * @updateBy modongning
	 * @updateDate 2018/8/3 下午2:14
	 * @param account
	 * @param password
	 * @param signTimes
	 * @return java.lang.String
	 * 密码加密
	 */
	public static String hash(String account,String password,int signTimes){
		return new Md5Hash(password, account, signTimes).toString();
	}
}
