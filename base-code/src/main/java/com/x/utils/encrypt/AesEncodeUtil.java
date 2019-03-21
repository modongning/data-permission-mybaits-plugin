package com.x.utils.encrypt;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 加解密工具
 * @author hzh 2018/7/8 下午11:31
 */
public class AesEncodeUtil {

	private static final String bm = "UTF-8";
	private static final String AES_KEY = "u3zTQzWGvWueqG5n";
	private static final String IV = "JlM6cyqmrC2zKNsx";

	/**
	 * 加密
	 * @param data
	 * @return
	 */
	public static String encrypt(String data) {
		return encrypt(data, AES_KEY, IV);
	}

	/**
	 * 加密
	 * @param data
	 * @param aesKey
	 * @param vi
	 * @return
	 */
	public static String encrypt(String data, String aesKey, String vi) {
		//加密方式： AES128(CBC/PKCS5Padding) + Base64, 私钥：aabbccddeeffgghh
		try {
			IvParameterSpec zeroIv = new IvParameterSpec(vi.getBytes());
			//两个参数，第一个为私钥字节数组， 第二个为加密方式 AES或者DES
			SecretKeySpec key = new SecretKeySpec(aesKey.getBytes(), "AES");
			//实例化加密类，参数为加密方式，要写全，PKCS5Padding比PKCS7Padding效率高，PKCS7Padding可支持IOS加解密
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			//初始化，此方法可以采用三种方式，按加密算法要求来添加。（1）无第三个参数（2）第三个参数为SecureRandom random = new SecureRandom();中random对象，随机数。(AES不可采用这种方法)（3）采用此代码中的IVParameterSpec
			cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
			//加密操作,返回加密后的字节数组，然后需要编码。主要编解码方式有Base64, HEX, UUE,7bit等等。此处看服务器需要什么编码方式
			byte[] encryptedData = cipher.doFinal(data.getBytes(bm));

			return new BASE64Encoder().encode(encryptedData).replaceAll("\\n", "");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 解密
	 * @param data
	 * @return
	 */
	public static String decrypt(String data) {
		return decrypt(data, AES_KEY, IV);
	}

	/**
	 * 解密
	 * @param data
	 * @param aesKey
	 * @param vi
	 * @return
	 */
	public static String decrypt(String data, String aesKey, String vi) {
		try {
			byte[] byteMi = new BASE64Decoder().decodeBuffer(data);
			IvParameterSpec zeroIv = new IvParameterSpec(vi.getBytes());
			SecretKeySpec key = new SecretKeySpec(
					aesKey.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			//与加密时不同MODE:Cipher.DECRYPT_MODE
			cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
			byte[] decryptedData = cipher.doFinal(byteMi);
			return new String(decryptedData, bm);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

}
