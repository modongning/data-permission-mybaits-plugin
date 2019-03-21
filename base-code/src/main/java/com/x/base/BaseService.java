package com.x.base;

import com.x.utils.data.Page;

import java.util.List;

public interface BaseService<M, T> {


	/**
	 * 根据ID查询
	 */
	M findById(T t);

	/**
	 * 列表查询
	 */
	List<M> query(M m);

	/**
	 * 分页查询
	 * {"pageNumber":0,"pageSize":10,...}
	 */
	<S,V> Page pageQuery(Page<S, V> page);

	/**
	 * 新增
	 */
	Long save(M m);

	/**
	 * 批量新增
	 */
	int saveBatch(List<M> list);

	/**
	 * 按主键更新全部字段
	 */
	int update(M m);

	/**
	 * 按主键删除
	 */
	int deleteById(T t);

}
