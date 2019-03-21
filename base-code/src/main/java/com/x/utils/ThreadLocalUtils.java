package com.x.utils;

import java.util.*;

/**
 * @Author modongning
 * @updateBy modongning
 * @updateBy 2018/7/7 下午5:36
 * 线程变量工具
 */
public class ThreadLocalUtils {
	private static final ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal() {
		protected Map<String, Object> initialValue() {
			return new HashMap(4);
		}
	};

	public static Map<String, Object> getThreadLocal(){
		return threadLocal.get();
	}

	/**
	 * 获取
	 * @param key
	 * @param <T>
	 * @return
	 */
	public static <T> T get(String key) {
		Map map = (Map)threadLocal.get();
		return (T)map.get(key);
	}

	/**
	 * 获取，没有值返回默认值
	 * @param key
	 * @param defaultValue
	 * @param <T>
	 * @return
	 */
	public static <T> T get(String key,T defaultValue) {
		Map map = (Map)threadLocal.get();
		return (T)map.get(key) == null ? defaultValue : (T)map.get(key);
	}

	/**
	 * 设置
	 * @param key
	 * @param value
	 */
	public static void set(String key, Object value) {
		Map map = (Map)threadLocal.get();
		map.put(key, value);
	}

	/**
	 * 批量设置
	 * @param keyValueMap
	 */
	public static void set(Map<String, Object> keyValueMap) {
		Map map = (Map)threadLocal.get();
		map.putAll(keyValueMap);
	}

	/**
	 * 移除
	 */
	public static void remove() {
		threadLocal.remove();
	}

	/**
	 * 根据前缀批量获取
	 * @param prefix
	 * @param <T>
	 * @return
	 */
	public static <T> Map<String,T> fetchVarsByPrefix(String prefix) {
		Map<String,T> vars = new HashMap<>();
		if( prefix == null ){
			return vars;
		}
		Map map = (Map)threadLocal.get();
		Set<Map.Entry> set = map.entrySet();

		for( Map.Entry entry : set ){
			Object key = entry.getKey();
			if( key instanceof String ){
				if( ((String) key).startsWith(prefix) ){
					vars.put((String)key,(T)entry.getValue());
				}
			}
		}
		return vars;
	}

	/**
	 * 根据KEY移除
	 * @param key
	 * @param <T>
	 * @return
	 */
	public static <T> T remove(String key) {
		Map map = (Map)threadLocal.get();
		return (T)map.remove(key);
	}

	/**
	 * 根据前缀移除
	 * @param prefix
	 */
	public static void clear(String prefix) {
		if( prefix == null ){
			return;
		}
		Map map = (Map)threadLocal.get();
		Set<Map.Entry> set = map.entrySet();
		List<String> removeKeys = new ArrayList<>();

		for( Map.Entry entry : set ){
			Object key = entry.getKey();
			if( key instanceof String ){
				if( ((String) key).startsWith(prefix) ){
					removeKeys.add((String)key);
				}
			}
		}
		for( String key : removeKeys ){
			map.remove(key);
		}
	}
}
